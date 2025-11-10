package com.rovits.app.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rovits.app.R

@Composable
fun TermsDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.terms_dialog_title)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = stringResource(R.string.terms_dialog_content),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text(stringResource(R.string.terms_dialog_accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.terms_dialog_cancel))
            }
        }
    )
}

