package com.rovits.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rovits.app.R

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onNavigateToLanguage: () -> Unit // YENİ
) {
    val logoutCompleted by viewModel.logoutCompleted.collectAsStateWithLifecycle()

    LaunchedEffect(logoutCompleted) {
        if (logoutCompleted) {
            onLogout()
            viewModel.onLogoutCompleted()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = stringResource(R.string.home_screen),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = onNavigateToLanguage,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(R.string.change_language)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.change_language))
            }

            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(stringResource(R.string.logout))
            }
        }
    }
}