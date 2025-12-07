package com.rovits.app.ui.components

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import com.rovits.app.utils.LocaleHelper

@Composable
fun LanguageSelectionDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val currentLanguage = LocaleHelper.getCurrentLanguageName(context)
    val english = stringResource(id = R.string.english)
    val turkish = stringResource(id = R.string.turkish)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.select_language))
        },
        text = {
            Column {
                Button(
                    onClick = {
                        onDismiss()
                        LocaleHelper.setLocale(context, LocaleHelper.LANGUAGE_ENGLISH)
                        (context as? Activity)?.recreate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentLanguage == english) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (currentLanguage == english) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(text = english)
                }
                Button(
                    onClick = {
                        onDismiss()
                        LocaleHelper.setLocale(context, LocaleHelper.LANGUAGE_TURKISH)
                        (context as? Activity)?.recreate()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentLanguage == turkish) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (currentLanguage == turkish) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(text = turkish)
                }
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

