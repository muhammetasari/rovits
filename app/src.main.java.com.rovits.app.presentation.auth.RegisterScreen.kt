package com.rovits.app.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rovits.app.R
import com.rovits.app.presentation.components.PasswordStrengthIndicator
import com.rovits.app.presentation.components.TermsDialog

// 3. UI (Composable Ekran)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
// ...existing code...
) {
    // ...existing code...

    // Password Strength Indicator
    PasswordStrengthIndicator(password = password)

    // Confirm Password Input
    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { confirmPassword = it },
        label = { Text("Şifreyi Onayla") },
        modifier = Modifier.fillMaxWidth(),
        isError = passwordError,
        supportingText = {
            if (passwordError) {
                Text(
                    text = "Şifreler uyuşmuyor",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}
