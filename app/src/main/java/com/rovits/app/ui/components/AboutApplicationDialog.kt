package com.rovits.app.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rovits.app.R
import com.rovits.app.ui.theme.RovitsAppTheme

@Composable
fun AboutApplicationDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(id = R.string.about_application_title),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.about_application_content),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.ok),)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AboutApplicationDialogPreview() {
    RovitsAppTheme {
        AboutApplicationDialog(
            onDismiss = {}
        )
    }
}