package com.rovits.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rovits.app.R
import com.rovits.app.data.model.User
import com.rovits.app.ui.common.StandardLayout
import com.rovits.app.ui.components.ListMenuItem
import com.rovits.app.ui.theme.RovitsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    user: User?,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToThemeDemo: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    StandardLayout(
        navController = navController,
        title = stringResource(id = R.string.account),
        showTopBar = true,
        showBackButton = false,
        showBottomBar = false,
        onNavigateBack = onNavigateBack,
        topAppBarActions = {
            IconButton(onClick = onNavigateToSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.content_description_settings),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ...existing code...
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.profile),
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                FloatingActionButton(
                    onClick = { /* Edit profile photo */ },
                    modifier = Modifier.size(36.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(id = R.string.content_description_edit),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Menu Items
            ListMenuItem(
                icon = Icons.Default.Person,
                title = stringResource(id = R.string.profile_details),
                hasTrailingIcon = true,
                onClick = { /* Navigate to profile details */ }
            )

            ListMenuItem(
                icon = Icons.Default.Lock,
                title = stringResource(id = R.string.password_settings),
                hasTrailingIcon = true,
                onClick = { /* Navigate to password */ }
            )

            ListMenuItem(
                icon = Icons.Default.Bookmark,
                title = stringResource(id = R.string.saved),
                hasTrailingIcon = true,
                onClick = { /* Navigate to saved */ }
            )

            ListMenuItem(
                icon = Icons.Default.Route,
                title = stringResource(id = R.string.journey),
                hasTrailingIcon = true,
                onClick = { /* Navigate to journey */ }
            )

            ListMenuItem(
                icon = Icons.Default.Palette,
                title = stringResource(id = R.string.theme_demo),
                hasTrailingIcon = true,
                onClick = onNavigateToThemeDemo
            )

            ListMenuItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = stringResource(id = R.string.logout),
                hasTrailingIcon = false,
                onClick = onLogout
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    RovitsAppTheme {
        ProfileScreen(
            navController = rememberNavController(),
            user = null,
            onLogout = {},
            onNavigateBack = {},
            onNavigateToThemeDemo = {}
        )
    }
}
