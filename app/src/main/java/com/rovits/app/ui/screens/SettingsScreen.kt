package com.rovits.app.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rovits.app.R
import com.rovits.app.data.model.AppThemeConfig
import com.rovits.app.ui.common.StandardLayout
import com.rovits.app.ui.theme.RovitsAppTheme
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rovits.app.ui.viewmodel.ThemeViewModel
import com.rovits.app.ui.components.ThemeSelectionDialog
import com.rovits.app.ui.components.AboutApplicationDialog
import com.rovits.app.ui.components.ListMenuItem
import com.rovits.app.ui.components.LanguageSelectionDialog
import com.rovits.app.ui.components.PermissionsControlDialog
import com.rovits.app.utils.LocaleHelper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    themeViewModel: ThemeViewModel? = null
) {
    val viewModel = themeViewModel ?: viewModel()
    val currentTheme by viewModel.themeConfig.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPermissionsDialog by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    SettingsScreenContent(
        onNavigateBack = onNavigateBack,
        currentTheme = currentTheme,
        showThemeDialog = showThemeDialog,
        onShowThemeDialog = { showThemeDialog = it },
        showLanguageDialog = showLanguageDialog,
        onShowLanguageDialog = { showLanguageDialog = it },
        showAboutDialog = showAboutDialog,
        onShowAboutDialog = { showAboutDialog = it },
        showPermissionsDialog = showPermissionsDialog,
        onShowPermissionsDialog = { showPermissionsDialog = it },
        onThemeSelected = { newTheme ->
            viewModel.updateThemeConfig(newTheme)
        },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    onNavigateBack: () -> Unit = {},
    currentTheme: AppThemeConfig = AppThemeConfig.FOLLOW_SYSTEM,
    showThemeDialog: Boolean = false,
    onShowThemeDialog: (Boolean) -> Unit = {},
    showLanguageDialog: Boolean = false,
    onShowLanguageDialog: (Boolean) -> Unit = {},
    showAboutDialog: Boolean = false,
    onShowAboutDialog: (Boolean) -> Unit = {},
    showPermissionsDialog: Boolean = false,
    onShowPermissionsDialog: (Boolean) -> Unit = {},
    onThemeSelected: (AppThemeConfig) -> Unit = {},
    navController: androidx.navigation.NavHostController? = null
)
 {
    val context = LocalContext.current
    StandardLayout(
        onNavigateBack = onNavigateBack,
        topAppBarTitle = stringResource(id = R.string.settings),
        showTopBar = true,
        showBackButton = true,
        showBottomBar = false,
        navController = navController ?: rememberNavController()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Menu Items
                    ListMenuItem(
                        onLongClick = { /* Navigate to theme demo */ },
                        icon = Icons.Default.DarkMode,
                        title = stringResource(id = R.string.dark_mode),
                        subtitle = currentTheme.getDisplayName(context),
                        onClick = { onShowThemeDialog(true) }

                    )
                    ListMenuItem(
                        icon = Icons.Outlined.Notifications,
                        title = stringResource(id = R.string.notification_settings),
                        onClick = { onShowPermissionsDialog(true) }
                    )

                    ListMenuItem(
                        icon = Icons.Default.GTranslate,
                        title = stringResource(id = R.string.language_settings),
                        subtitle = LocaleHelper.getCurrentLanguageName(context),
                        onClick = { onShowLanguageDialog(true) }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Menu Items
                    ListMenuItem(
                        icon = Icons.Outlined.Info,
                        title = stringResource(id = R.string.about_application),
                        hasTrailingIcon = true,
                        onClick = { onShowAboutDialog(true) }
                    )
                    ListMenuItem(
                        icon = Icons.Outlined.Feedback,
                        title = stringResource(id = R.string.feedback),
                        hasTrailingIcon = true,
                        onClick = { /* Navigate to feedback */ }
                    )

                    ListMenuItem(
                        icon = Icons.Default.Support,
                        title = stringResource(id = R.string.support),
                        hasTrailingIcon = true,
                        onClick = { /* Navigate to Support */ }
                    )
                }
            }
        }

        // Theme Selection Dialog
        if (showThemeDialog) {
            ThemeSelectionDialog(
                currentTheme = currentTheme,
                onThemeSelected = { newTheme ->
                    onThemeSelected(newTheme)
                    onShowThemeDialog(false)
                },
                onDismiss = { onShowThemeDialog(false) }
            )
        }

        //Language Selection Dialog
        if (showLanguageDialog) {
            LanguageSelectionDialog(
                onDismiss = { onShowLanguageDialog(false) }
            )
        }

        // About Application Dialog
        if (showAboutDialog) {
            AboutApplicationDialog(
                onDismiss = { onShowAboutDialog(false) }
            )
        }
        if (showPermissionsDialog) {
            PermissionsControlDialog(
                onDismiss = { onShowPermissionsDialog(false) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    RovitsAppTheme {
        SettingsScreenContent(
            onNavigateBack = { /* no-op */ }
        )
    }
}