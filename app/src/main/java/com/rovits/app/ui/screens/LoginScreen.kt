package com.rovits.app.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import com.rovits.app.ui.components.*
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToForgotPassword: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onLoginClick: (email: String, password: String) -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showLanguageMenu by remember { mutableStateOf(false) }

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
                        AlertDialog(
                            onDismissRequest = { showLanguageMenu = false },
                            title = {
                                Text(text = stringResource(id = R.string.select_language))
                            },
                            text = {
                                Column {
                                    Button(
                                        onClick = { showLanguageMenu = false },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    ) {
                                        Text("English")
                                    }
                                    Button(
                                        onClick = { showLanguageMenu = false },
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = stringResource(id = R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email/Username TextField
            RovitsTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(id = R.string.email_or_username)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            RovitsTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(id = R.string.password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            LoginButton(
                text = stringResource(id = R.string.login),
                onClick = { onLoginClick(email, password) }
            )

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
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign In Button
            SocialLoginButton(onClick = onGoogleSignInClick)

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
            onNavigateToForgotPassword = {},
            onNavigateToRegister = {},
            onLoginClick = { _, _ -> },
            onGoogleSignInClick = {}
        )
    }
}
