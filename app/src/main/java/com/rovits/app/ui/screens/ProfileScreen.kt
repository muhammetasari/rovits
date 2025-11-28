package com.rovits.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
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
import com.rovits.app.data.model.User
import com.rovits.app.ui.theme.RovitsAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User?,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToThemeDemo: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.account),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Settings action */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.content_description_settings),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
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

            // Profile Photo Section
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                // Profile Image
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

                // Edit Button
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
            ProfileMenuItem(
                icon = Icons.Default.Person,
                title = stringResource(id = R.string.profile_details),
                hasTrailingIcon = true,
                onClick = { /* Navigate to profile details */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.Lock,
                title = stringResource(id = R.string.password_settings),
                hasRedDot = true,
                hasTrailingIcon = true,
                onClick = { /* Navigate to password */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.Bookmark,
                title = stringResource(id = R.string.saved),
                hasTrailingIcon = true,
                onClick = { /* Navigate to saved */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.Route,
                title = stringResource(id = R.string.journey),
                hasTrailingIcon = true,
                onClick = { /* Navigate to journey */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.Palette,
                title = stringResource(id = R.string.theme_demo),
                hasTrailingIcon = true,
                onClick = onNavigateToThemeDemo
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.Logout,
                title = stringResource(id = R.string.logout),
                hasTrailingIcon = false,
                onClick = onLogout
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    hasRedDot: Boolean = false,
    hasTrailingIcon: Boolean = false,
    onClick: () -> Unit
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

            // Red Dot Indicator
            if (hasRedDot) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Trailing Arrow Icon
            if (hasTrailingIcon) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = stringResource(id = R.string.content_description_navigate),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    RovitsAppTheme {
        ProfileScreen(
            user = null,
            onLogout = {},
            onNavigateBack = {},
            onNavigateToThemeDemo = {}
        )
    }
}

