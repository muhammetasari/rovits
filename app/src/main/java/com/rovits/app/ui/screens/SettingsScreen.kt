package com.rovits.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rovits.app.R
import com.rovits.app.ui.common.StandardLayout
import com.rovits.app.ui.theme.RovitsAppTheme
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val navController = rememberNavController()
    StandardLayout(
        onNavigateBack = onNavigateBack,
        topAppBarTitle = stringResource(id = R.string.settings),
        showTopBar = true,
        showBackButton = true,
        showBottomBar = false,
        navController = navController
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
                    SettingsMenuItem(
                        icon = Icons.Default.DarkMode,
                        title = stringResource(id = R.string.dark_mode),
                        hasTrailingIcon = false,
                        showSwitchesButton = true,
                        onClick = { /* Navigate to theme demo */ }
                    )
                    SettingsMenuItem(
                        icon = Icons.Outlined.Notifications,
                        title = stringResource(id = R.string.notification_settings),
                        hasTrailingIcon = true,
                        onClick = { /* Navigate to profile details */ }
                    )

                    SettingsMenuItem(
                        icon = Icons.Default.GTranslate,
                        title = stringResource(id = R.string.language_settings),
                        hasTrailingIcon = true,
                        onClick = { /* Navigate to password */ }
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
                    SettingsMenuItem(
                        icon = Icons.Outlined.Info,
                        title = stringResource(id = R.string.about_application),
                        hasTrailingIcon = true,
                        onClick = { /* Navigate to theme demo */ }
                    )
                    SettingsMenuItem(
                        icon = Icons.Outlined.Feedback,
                        title = stringResource(id = R.string.feedback),
                        hasTrailingIcon = true,
                        onClick = { /* Navigate to feedback */ }
                    )

                    SettingsMenuItem(
                        icon = Icons.Default.Support,
                        title = stringResource(id = R.string.support),
                        hasTrailingIcon = true,
                        onClick = { /* Navigate to Support */ }
                    )
                }
            }

        }
    }
}

@Composable
fun SettingsMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    hasTrailingIcon: Boolean = false,
    showSwitchesButton: Boolean = false,
    switchState: Boolean = false,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Trailing Arrow Icon
            if (hasTrailingIcon) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = stringResource(id = R.string.content_description_navigate),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (showSwitchesButton) {
                var localSwitchState by remember { mutableStateOf(switchState) }
                Switch(
                    checked = localSwitchState,
                    onCheckedChange = { localSwitchState = it },

                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    RovitsAppTheme {
        SettingsScreen(
            onNavigateBack = { /* no-op */ }
        )
    }
}
