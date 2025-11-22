package com.rovits.app.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rovits.app.R
import com.rovits.app.presentation.components.PasswordStrengthIndicator
import com.rovits.app.presentation.components.TermsDialog

// 3. UI (Composable Ekran)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()
    val isLoading = registerState is RegisterState.Loading

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) } // YENİ
    var hasReadTerms by remember { mutableStateOf(false) } // YENİ

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Email validation
    val emailError = remember(email) {
        when {
            email.isEmpty() -> null
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                context.getString(R.string.error_invalid_email)
            else -> null
        }
    }

    val passwordsMatch = remember(password, confirmPassword) {
        password == confirmPassword && confirmPassword.isNotEmpty()
    }

    // Form validation
    val isFormValid = remember(name, email, emailError, passwordsMatch, termsAccepted) {
        name.isNotEmpty()
                && email.isNotEmpty()
                && emailError == null
                && password.isNotEmpty()
                && passwordsMatch
                && termsAccepted
    }

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success) {
            onRegisterSuccess()
            viewModel.resetState()
        }
    }

    if (showTermsDialog) {
        TermsDialog(
            onDismiss = { showTermsDialog = false },
            onAccept = {
                hasReadTerms = true
                termsAccepted = true
                showTermsDialog = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text(stringResource(id = R.string.register)) },
            navigationIcon = {
                IconButton(onClick = onBackToLogin, enabled = !isLoading) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.full_name)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )

            // Email Input
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.email)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                isError = emailError != null,
                supportingText = emailError?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )
            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(id = R.string.password)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                stringResource(id = R.string.hide_password)
                            else
                                stringResource(id = R.string.show_password)
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading
            )
            // Password Strength Indicator
            PasswordStrengthIndicator(password = password)

            // Confirm Password Input
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(id = R.string.confirm_password)) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible)
                                stringResource(id = R.string.hide_password)
                            else
                                stringResource(id = R.string.show_password)
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (isFormValid) {
                            viewModel.registerWithEmailPassword(name, email, password)
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !isLoading,
                isError = !passwordsMatch && confirmPassword.isNotEmpty(),
                supportingText = if (!passwordsMatch && confirmPassword.isNotEmpty()) {
                    { Text(stringResource(id = R.string.passwords_do_not_match), color = MaterialTheme.colorScheme.error) }
                } else null
            )
            // Terms & Conditions - GÜNCELLENDİ
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = {
                        if (!hasReadTerms) {
                            // Okumadıysa dialog aç
                            showTermsDialog = true
                        } else {
                            // Okumuşsa checkbox toggle edilebilir
                            termsAccepted = it
                        }
                    },
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = { showTermsDialog = true },
                    enabled = !isLoading
                ) {
                    Text(
                        text = stringResource(id = R.string.terms_and_conditions),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Register Button
            Button(
                onClick = { viewModel.registerWithEmailPassword(name, email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && isFormValid
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(id = R.string.register))
                }
            }

            // Hata mesajı
            (registerState as? RegisterState.Error)?.let {
                Text(
                    text = it.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
