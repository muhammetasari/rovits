package com.rovits.app.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rovits.app.util.Language

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    viewModel: LanguageViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.language_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_button))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Language.entries.forEach { language ->
                LanguageItem(
                    language = language,
                    isSelected = currentLanguage == language,
                    onClick = {
                        viewModel.changeLanguage(language, context)
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(language.stringResId),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = language.code.uppercase(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}