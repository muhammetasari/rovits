package com.rovits.app.ui.screens

import android.app.Activity
import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import com.rovits.app.data.repository.AuthRepository
import com.rovits.app.ui.components.*
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.theme.TextSecondary
import com.rovits.app.ui.viewmodel.AuthViewModel
import com.rovits.app.utils.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showLanguageMenu by remember { mutableStateOf(false) }
    var isEmailValid by remember { mutableStateOf(false) }
    var emailTouched by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle authentication state
    LaunchedEffect(authState.isSuccess) {
        if (authState.isSuccess) {
            viewModel.clearSuccess()
            onLoginSuccess()
        }
    }

    // Handle errors
    LaunchedEffect(authState.error) {
        authState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    // Show dialogs
    if (showTermsDialog) {
        TermsOfUseDialog(onDismiss = { showTermsDialog = false })
    }
    if (showPrivacyDialog) {
        PrivacyPolicyDialog(onDismiss = { showPrivacyDialog = false })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = { showLanguageMenu = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.language_24),
                            contentDescription = stringResource(id = R.string.language)
                        )
                    }
                    if (showLanguageMenu) {
                        val currentLanguage = LocaleHelper.getCurrentLanguageName(context)
                        AlertDialog(
                            onDismissRequest = { showLanguageMenu = false },
                            title = {
                                Text(text = stringResource(id = R.string.select_language))
                            },
                            text = {
                                Column {
                                    Button(
                                        onClick = {
                                            showLanguageMenu = false
                                            LocaleHelper.setLocale(context, LocaleHelper.LANGUAGE_ENGLISH)
                                            (context as? Activity)?.recreate()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (currentLanguage == "English") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            contentColor = if (currentLanguage == "English") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        Text("English")
                                    }
                                    Button(
                                        onClick = {
                                            showLanguageMenu = false
                                            LocaleHelper.setLocale(context, LocaleHelper.LANGUAGE_TURKISH)
                                            (context as? Activity)?.recreate()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (currentLanguage == "Türkçe") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                            contentColor = if (currentLanguage == "Türkçe") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    ) {
                                        Text("Türkçe")
                                    }
                                }
                            },
                            confirmButton = {},
                            dismissButton = {}
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            RovitsLogo(size = 120.dp)

            Spacer(modifier = Modifier.height(60.dp))



            // Email/Username TextField
            RovitsTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailTouched = true
                    isEmailValid = Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                placeholder = stringResource(id = R.string.email),
                enabled = !authState.isLoading,
                isError = !isEmailValid && emailTouched,
                errorMessage = if (!isEmailValid && emailTouched) stringResource(id = R.string.error_invalid_email_format) else null
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            RovitsTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(id = R.string.password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                enabled = !authState.isLoading,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            TextButton(
                onClick = onNavigateToForgotPassword,
                modifier = Modifier.align(Alignment.End),
                enabled = !authState.isLoading
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    if (isEmailValid) {
                        viewModel.signInWithEmail(email, password, context)
                    }
                },
                enabled = !authState.isLoading && isEmailValid && password.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (authState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Text
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.no_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                TextButton(
                    onClick = onNavigateToRegister,
                    enabled = !authState.isLoading
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign In Button
            SocialLoginButton(
                onClick = onGoogleSignInClick,
                enabled = !authState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Terms and Privacy
            TermsPrivacyText(
                onTermsClick = { showTermsDialog = true },
                onPrivacyClick = { showPrivacyDialog = true },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    RovitsAppTheme {
        LoginScreen(
            viewModel = AuthViewModel(AuthRepository()),
            onNavigateToForgotPassword = {},
            onNavigateToRegister = {},
            onLoginSuccess = {},
            onGoogleSignInClick = {}
        )
    }
}
