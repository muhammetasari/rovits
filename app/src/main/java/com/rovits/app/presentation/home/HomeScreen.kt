package com.rovits.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    onNavigateToLanguage: () -> Unit
) {
    val logoutState by viewModel.logoutState.collectAsStateWithLifecycle()

    // Logout başarılı olunca navigate et
    LaunchedEffect(logoutState) {
        if (logoutState is LogoutState.Success) {
            onLogout()
            viewModel.resetLogoutState()
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

            // Language Button
            OutlinedButton(
                onClick = onNavigateToLanguage,
                modifier = Modifier.fillMaxWidth(0.6f),
                enabled = logoutState !is LogoutState.Loading
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = stringResource(R.string.change_language)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.change_language))
            }

            // Logout Button
            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(0.6f),
                enabled = logoutState !is LogoutState.Loading
            ) {
                if (logoutState is LogoutState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.logout))
                }
            }

            // Error message (nadiren gösterilir)
            if (logoutState is LogoutState.Error) {
                Text(
                    text = (logoutState as LogoutState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}