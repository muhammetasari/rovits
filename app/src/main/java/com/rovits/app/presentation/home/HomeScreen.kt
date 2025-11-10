package com.rovits.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onNavigateToLanguage: () -> Unit
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
                text = "Welcome to Rovits!",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Home Screen",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Dil değiştirme butonu
            OutlinedButton(
                onClick = onNavigateToLanguage,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Dil Ayarları")
            }

            Button(
                onClick = { viewModel.logout() },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("Çıkış Yap")
            }
        }
    }
}