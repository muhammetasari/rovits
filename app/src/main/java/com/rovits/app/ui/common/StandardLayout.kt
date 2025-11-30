package com.rovits.app.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardLayout(
    onNavigateBack: () -> Unit,
    topAppBarTitle: String = "",
    showTopBar: Boolean = true,
    showBackButton: Boolean = true,
    showBottomBar: Boolean = true,
    navController: NavController,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            if (showTopBar) {
                StandartTopAppBar(
                    title = topAppBarTitle,
                    showBackButton = showBackButton,
                    onNavigateBack = onNavigateBack
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                StandardBottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

/**
 * StandardLayout için önizleme fonksiyonu.
 */
@Preview(showBackground = true)
@Composable
fun StandardLayoutPreview() {
    val navController = rememberNavController()
    StandardLayout(
        onNavigateBack = {},
        topAppBarTitle = "Başlık",
        showTopBar = true,
        showBackButton = true,
        showBottomBar = true,
        navController = navController
    ) { paddingValues ->
        // Örnek içerik
    }
}
