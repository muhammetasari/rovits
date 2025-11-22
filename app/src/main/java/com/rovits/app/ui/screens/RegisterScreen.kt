package com.rovits.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun RegisterScreen(
    onBackPressed: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onRegisterClick: (fullName: String, email: String, password: String) -> Unit,
    onGoogleSignInClick: () -> Unit,
    onLanguageClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

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
                            contentDescription = "Back"
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
            RovitsLogo(size = 100.dp)

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = stringResource(id = R.string.register_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Full Name TextField
            RovitsTextField(
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = stringResource(id = R.string.full_name)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email TextField
            RovitsTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(id = R.string.email)
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

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            LoginButton(
                text = stringResource(id = R.string.register),
                onClick = { onRegisterClick(fullName, email, password) }
            )

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
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google Sign In Button
            SocialLoginButton(onClick = onGoogleSignInClick)

            Spacer(modifier = Modifier.height(16.dp))

            // Sign In Text
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                TextButton(onClick = onNavigateToLogin) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RovitsAppTheme {
        RegisterScreen(
            onBackPressed = {},
            onNavigateToLogin = {},
            onRegisterClick = { _, _, _ -> },
            onGoogleSignInClick = {},
            onLanguageClick = {}
        )
    }
}

