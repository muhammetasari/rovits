package com.rovits.app.ui.screens.authscreen

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import com.rovits.app.data.repository.AuthRepository
import com.rovits.app.ui.components.*
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onBackPressed: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onGoogleSignInClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val isEmailValid = remember(email) {
        email.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle authentication state
    LaunchedEffect(authState.isSuccess) {
        if (authState.isSuccess) {
            viewModel.clearSuccess()
            onRegisterSuccess()
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
                        text = stringResource(id = R.string.register),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back)
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
            RovitsLogo(size = 240.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = stringResource(id = R.string.register_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name TextField
            RovitsTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = stringResource(id = R.string.full_name),
                enabled = !authState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email TextField
            RovitsTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(id = R.string.email),
                enabled = !authState.isLoading,
                isError = !isEmailValid,
                errorMessage = if (!isEmailValid) stringResource(id = R.string.error_invalid_email_format) else null
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
                            contentDescription = if (passwordVisible) stringResource(id = R.string.content_description_hide_password) else stringResource(id = R.string.content_description_show_password)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password strength indicator
            PasswordStrengthIndicator(password = password)

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Button(
                onClick = { viewModel.signUpWithEmail(fullName, email, password, context) },
                enabled = !authState.isLoading && fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && isEmailValid,
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
                        text = stringResource(id = R.string.register),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Or Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.or),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign In Button
            SocialLoginButton(
                onClick = onGoogleSignInClick,
                enabled = !authState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Text
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onNavigateToLogin,
                    enabled = !authState.isLoading
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_in),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

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

@Composable
private fun PasswordStrengthIndicator(password: String) {
    if (password.isEmpty()) return

    val score by remember(password) { mutableStateOf(calculatePasswordScore(password)) }

    val (label, color) = when {
        score < 0.34f -> stringResource(id = R.string.password_weak) to MaterialTheme.colorScheme.error
        score < 0.67f -> stringResource(id = R.string.password_medium) to MaterialTheme.colorScheme.primary
        else -> stringResource(id = R.string.password_strong) to MaterialTheme.colorScheme.secondary
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp)) {
        LinearProgressIndicator(
            progress = { score },
            color = color,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

private fun calculatePasswordScore(password: String): Float {
    var score = 0f
    // length weight
    score += when {
        password.length >= 12 -> 0.4f
        password.length >= 8 -> 0.25f
        password.length >= 5 -> 0.12f
        else -> 0f
    }
    // has digit
    if (password.any { it.isDigit() }) score += 0.2f
    // has uppercase
    if (password.any { it.isUpperCase() }) score += 0.2f
    // has special char
    if (password.any { !it.isLetterOrDigit() }) score += 0.2f

    return score.coerceIn(0f, 1f)
}
