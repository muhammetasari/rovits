package com.rovits.app.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardLayout(
    navController: NavController,
    title: String? = null,
    showTopBar: Boolean = true,
    showBackButton: Boolean = true,
    showBottomBar: Boolean = false,
    onNavigateBack: () -> Unit = { navController.popBackStack() },
    topAppBarActions: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { title?.let { Text(it) } },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = {
                        topAppBarActions()
                    },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                bottomBar()
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
    RovitsAppTheme {
        StandardLayout(
            navController = rememberNavController(),
            title = "Test Başlığı",
            showTopBar = true,
            showBackButton = true,
            showBottomBar = false
        ) { paddingValues ->
            Text(
                text = "İçerik",
                modifier = androidx.compose.foundation.layout.Modifier
                    .padding(paddingValues)
            )
        }
    }
}

