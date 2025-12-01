package com.rovits.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.rovits.app.data.model.AppThemeConfig

/**
 * A Material 3 dialog for theme selection.
 *
 * Displays three radio button options:
 * - System Default (FOLLOW_SYSTEM)
 * - Light theme
 * - Dark theme
 *
 * @param currentTheme The currently selected theme configuration
 * @param onThemeSelected Callback invoked when a theme option is selected
 * @param onDismiss Callback invoked when the dialog should be dismissed
 */
@Composable
fun ThemeSelectionDialog(
    currentTheme: AppThemeConfig,
    onThemeSelected: (AppThemeConfig) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Choose Theme",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AppThemeConfig.entries.forEach { themeConfig ->
                    ThemeOption(
                        theme = themeConfig,
                        isSelected = currentTheme == themeConfig,
                        onOptionSelected = { onThemeSelected(themeConfig) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * A single theme option row with a radio button.
 *
 * @param theme The theme configuration this option represents
 * @param isSelected Whether this option is currently selected
 * @param onOptionSelected Callback invoked when this option is clicked
 */
@Composable
private fun ThemeOption(
    theme: AppThemeConfig,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = isSelected,
                onClick = onOptionSelected,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null // null because the entire row is clickable
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = theme.getDisplayName(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

