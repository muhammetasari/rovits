package com.rovits.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rovits.app.R

@Composable
fun PasswordStrengthIndicator(password: String) {
    if (password.isEmpty()) return

    val strength = remember(password) {
        when {
            password.length < 6 -> PasswordStrength.WEAK
            password.length < 8 -> PasswordStrength.MEDIUM
            password.length >= 8 &&
                    password.any { it.isDigit() } &&
                    password.any { it.isUpperCase() } &&
                    password.any { !it.isLetterOrDigit() } -> PasswordStrength.STRONG
            password.length >= 8 &&
                    password.any { it.isDigit() } &&
                    password.any { it.isUpperCase() } -> PasswordStrength.GOOD
            else -> PasswordStrength.MEDIUM
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LinearProgressIndicator(
                progress = { strength.progress },
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp),
                color = strength.color,
            )
            Text(
                text = stringResource(strength.labelResId),
                style = MaterialTheme.typography.labelSmall,
                color = strength.color
            )
        }

        if (strength != PasswordStrength.STRONG && strength.hintResId != 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(strength.hintResId),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private enum class PasswordStrength(
    val labelResId: Int,
    val progress: Float,
    val color: Color,
    val hintResId: Int
) {
    WEAK(
        labelResId = R.string.password_strength_weak,
        progress = 0.25f,
        color = Color(0xFFE53935),
        hintResId = R.string.password_hint_min_length
    ),
    MEDIUM(
        labelResId = R.string.password_strength_medium,
        progress = 0.5f,
        color = Color(0xFFFB8C00),
        hintResId = R.string.password_hint_medium
    ),
    GOOD(
        labelResId = R.string.password_strength_good,
        progress = 0.75f,
        color = Color(0xFF43A047),
        hintResId = R.string.password_hint_good
    ),
    STRONG(
        labelResId = R.string.password_strength_strong,
        progress = 1f,
        color = Color(0xFF2E7D32),
        hintResId = 0
    )
}

