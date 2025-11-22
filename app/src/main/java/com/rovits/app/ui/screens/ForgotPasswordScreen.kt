package com.rovits.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import com.rovits.app.ui.components.*
import com.rovits.app.ui.theme.RovitsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackPressed: () -> Unit,
    onSendResetLink: (email: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

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
                        text = stringResource(id = R.string.forgot_password_title),
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
            RovitsLogo(size = 120.dp)

            Spacer(modifier = Modifier.height(48.dp))

            // Email/Username TextField
            RovitsTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = stringResource(id = R.string.email_or_username)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Send Reset Link Button
            LoginButton(
                text = stringResource(id = R.string.send_reset_link),
                onClick = { onSendResetLink(email) }
            )

            Spacer(modifier = Modifier.height(32.dp))

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
fun ForgotPasswordScreenPreview() {
    RovitsAppTheme {
        ForgotPasswordScreen(
            onBackPressed = {},
            onSendResetLink = {}
        )
    }
}

