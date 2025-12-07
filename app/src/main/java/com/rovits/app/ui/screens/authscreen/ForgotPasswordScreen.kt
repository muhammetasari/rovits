package com.rovits.app.ui.screens.authscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.rovits.app.R
import com.rovits.app.ui.common.StandardLayout
import com.rovits.app.ui.components.RovitsLogo
import com.rovits.app.ui.components.TermsOfUseDialog
import com.rovits.app.ui.components.PrivacyPolicyDialog
import com.rovits.app.ui.components.TermsPrivacyText
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    onBackPressed: () -> Unit,
    onResetSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val isEmailValid = email.isNotBlank() && isValidEmail(email)
    val isEmailInvalid = email.isNotBlank() && !isEmailValid

    // Handle success
    LaunchedEffect(authState.isSuccess) {
        if (authState.isSuccess) {
            snackbarHostState.showSnackbar(context.getString(R.string.success_password_reset))
            viewModel.clearSuccess()
            onResetSuccess()
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

    StandardLayout(
        navController = navController,
        title = stringResource(id = R.string.forgot_password_title),
        showTopBar = true,
        showBackButton = true,
        showBottomBar = false,
        onNavigateBack = onBackPressed,
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
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
        ) {
            // Logo
            RovitsLogo(size = 240.dp)

            Spacer(modifier = Modifier.height(48.dp))

            // Email/Username TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.email_or_username),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !authState.isLoading,
                singleLine = true,
                isError = isEmailInvalid,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error
                ),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            // Error message
            if (isEmailInvalid) {
                Text(
                    text = stringResource(id = R.string.error_invalid_email_format),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 0.dp, top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Send Reset Link Button
            Button(
                onClick = { viewModel.sendPasswordResetEmail(email, context) },
                enabled = !authState.isLoading && isEmailValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
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
                        text = stringResource(id = R.string.send_reset_link),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

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

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    RovitsAppTheme {
        val navController = rememberNavController()
        ForgotPasswordScreen(
            navController = navController,
            viewModel = AuthViewModel(repository = com.rovits.app.data.repository.fake.FakeAuthRepository()),
            onBackPressed = {},
            onResetSuccess = {}
        )
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

