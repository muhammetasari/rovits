package com.rovits.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rovits.app.R
import com.rovits.app.ui.common.StandardLayout
import com.rovits.app.ui.theme.RovitsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var lastBackPressTime by remember { mutableStateOf(0L) }
    var showExitDialog by remember { mutableStateOf(false) }
    val exitTitle = stringResource(id = R.string.exit_app_title)
    val exitMessage = stringResource(id = R.string.exit_app_message)
    val yesText = stringResource(id = R.string.yes)
    val noText = stringResource(id = R.string.no)

    // Geri tuşu yakalama
    BackHandler(enabled = true) {
        val now = System.currentTimeMillis()
        if (now - lastBackPressTime < 500) {
            showExitDialog = true
        } else {
            lastBackPressTime = now
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(exitTitle) },
            text = { Text(exitMessage) },
            confirmButton = {
                TextButton(onClick = { showExitDialog = false; (context as? android.app.Activity)?.finish() }) {
                    Text(yesText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(noText)
                }
            }
        )
    }

    StandardLayout(
        showTopBar = false,
        showBottomBar = true,
        onNavigateBack = { /* Ana sayfa, geri gitmez */ },
        navController = navController,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Content()
        }
    }
}

@Composable
fun Content() {
    Text(
        text = "Ana Sayfa İçeriği",
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDesignPreview() {
    RovitsAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}
