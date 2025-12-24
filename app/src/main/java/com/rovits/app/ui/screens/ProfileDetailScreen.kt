package com.rovits.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
fun ProfileDetailScreen(
    navController: NavController,
    user: User?,
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: () -> Unit = {}
) {
    StandardLayout(
        navController = navController,
        title = stringResource(id = R.string.edit_profile_title),
        showTopBar = true,
        showBackButton = true,
        showBottomBar = false,
        onNavigateBack = onNavigateBack,
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
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
                title = stringResource(id = R.string.name),
                subtitle = user?.fullName ?: stringResource(id = R.string.guest),
                hasTrailingIcon = true,
                onClick = {}
            )
            ListMenuItem(
                icon = Icons.Default.Email,
                title = stringResource(id = R.string.email),
                subtitle = user?.email ?: stringResource(id = R.string.guest),
                hasTrailingIcon = true,
                onClick = {}
            )

            // Change Password - Yalnızca email/password ile giriş yapan kullanıcılar için aktif
            val isPasswordChangeEnabled = user?.isPasswordProvider == true && user.isAnonymous == false

            ListMenuItem(
                icon = Icons.Default.Password,
                title = stringResource(id = R.string.change_password),
                subtitle = when {
                    user == null -> null
                    user.isAnonymous -> stringResource(id = R.string.change_password_disabled_guest)
                    !user.isPasswordProvider -> stringResource(id = R.string.change_password_disabled_google)
                    else -> null
                },
                hasTrailingIcon = isPasswordChangeEnabled,
                enabled = isPasswordChangeEnabled,
                onClick = onNavigateToChangePassword
            )

        }
    }
}


@Preview(showBackground = true, name = "Email/Password User")
@Composable
fun ProfileDetailPreview() {
    RovitsAppTheme {
        ProfileDetailScreen(
            navController = rememberNavController(),
            user = User(
                uid = "preview_user_123",
                fullName = "Ali Sarı",
                email = "ali.sari@rovits.com",
                photoUrl = null,
                isPasswordProvider = true,
                isAnonymous = false
            ),
            onNavigateBack = {},
            onNavigateToChangePassword = {}
        )
    }
}

@Preview(showBackground = true, name = "Google User")
@Composable
fun ProfileDetailGoogleUserPreview() {
    RovitsAppTheme {
        ProfileDetailScreen(
            navController = rememberNavController(),
            user = User(
                uid = "google_user_456",
                fullName = "Ayşe Yılmaz",
                email = "ayse.yilmaz@gmail.com",
                photoUrl = null,
                isPasswordProvider = false,
                isAnonymous = false
            ),
            onNavigateBack = {},
            onNavigateToChangePassword = {}
        )
    }
}
