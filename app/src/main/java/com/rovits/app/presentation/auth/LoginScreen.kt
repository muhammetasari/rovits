package com.rovits.app.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rovits.app.R
import com.rovits.app.presentation.settings.LanguageViewModel
import com.rovits.app.util.Language
import kotlinx.coroutines.launch
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    languageViewModel: LanguageViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val loginState by viewModel.loginState.collectAsStateWithLifecycle()
    val currentLanguage by languageViewModel.currentLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showLanguageMenu by remember { mutableStateOf(false) }

    val isLoading = loginState is LoginState.Loading

    // Google Sign-In Request hazır olduğunda başlat
    LaunchedEffect(loginState) {
        if (loginState is LoginState.GoogleSignInRequestReady) {
            val state = loginState as LoginState.GoogleSignInRequestReady
            scope.launch {
                try {
                    val result = state.credentialManager.getCredential(
                        request = state.request,
                        context = context
                    )
                    viewModel.handleGoogleSignInResult(result.credential)
                } catch (_: GetCredentialCancellationException) {
                    Log.d("LoginScreen", "Google Sign-In cancelled by user")
                    viewModel.resetState()
                } catch (e: GetCredentialException) {
                    Log.e("LoginScreen", "Google Sign-In credential error", e)
                    viewModel.resetState()
                }
            }
        }
    }

    // Login success durumunda navigate et
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    Box {
                        IconButton(onClick = { showLanguageMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = stringResource(R.string.language_screen_title),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        DropdownMenu(
                            expanded = showLanguageMenu,
                            onDismissRequest = { showLanguageMenu = false }
                        ) {
                            Language.entries.forEach { language ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = stringResource(language.stringResId))
                                            if (currentLanguage == language) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        languageViewModel.changeLanguage(language, context)
                                        showLanguageMenu = false
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0f)
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.password)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = { viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(id = R.string.login))
                }
            }

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.or),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            // Google Login Button
            OutlinedButton(
                onClick = { viewModel.signInWithGoogle() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (loginState is LoginState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(stringResource(id = R.string.continue_with_google))
                }
            }


            TextButton(
                onClick = onNavigateToRegister,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                Text(stringResource(id = R.string.no_account_register))
            }

            // Error Messages
            val errorMessage = (loginState as? LoginState.Error)?.message

            if (errorMessage != null) {
                Log.e("LoginScreen", "Login error: $errorMessage")
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
    }
}