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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.rovits.app.R
import com.rovits.app.data.model.User
import com.rovits.app.data.repository.fake.FakeUserRepository
import com.rovits.app.ui.common.StandardLayout
import com.rovits.app.ui.components.ListMenuItem
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.viewmodel.ProfileDetailUiState
import com.rovits.app.ui.viewmodel.ProfileDetailViewModel

/**
 * Profil detay ekranı. Kullanıcı bilgilerini gösterir ve düzenleme işlemlerini yönetir.
 * ViewModel ve UI state ile profesyonel şekilde yönetilir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(
    navController: NavController,
    viewModel: ProfileDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToChangePassword: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    StandardLayout(
        navController = navController,
        title = stringResource(id = R.string.edit_profile_title),
        showTopBar = true,
        showBackButton = true,
        showBottomBar = false,
        onNavigateBack = onNavigateBack,
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (uiState) {
                is ProfileDetailUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is ProfileDetailUiState.Error -> {
                    val message = (uiState as ProfileDetailUiState.Error).message
                    LaunchedEffect(message) {
                        snackbarHostState.showSnackbar(message)
                    }
                }
                is ProfileDetailUiState.Success -> {
                    val user = (uiState as ProfileDetailUiState.Success).user
                    ProfileDetailContent(
                        user = user,
                        onNavigateToChangePassword = onNavigateToChangePassword
                    )
                }
            }
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun ProfileDetailContent(
    user: User?,
    onNavigateToChangePassword: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        ProfileAvatar(user = user)
        Spacer(modifier = Modifier.height(32.dp))
        ProfileMenuItems(user = user, onNavigateToChangePassword = onNavigateToChangePassword)
    }
}

@Composable
private fun ProfileAvatar(user: User?) {
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (!user?.photoUrl.isNullOrBlank()) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = stringResource(id = R.string.profile),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_person_placeholder),
                error = painterResource(id = R.drawable.ic_person_placeholder)
            )
        } else {
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
        }
        // Sadece e-posta/şifre ile giriş yapanlar için düzenle butonu
        if (user?.isPasswordProvider == true) {
            FloatingActionButton(
                onClick = { /* Profil fotoğrafı düzenle */ },
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
    }
}

@Composable
private fun ProfileMenuItems(
    user: User?,
    onNavigateToChangePassword: () -> Unit
) {
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

@Preview(showBackground = true, name = "Email/Password User")
@Composable
fun ProfileDetailPreview() {
    RovitsAppTheme {
        val fakeRepo = FakeUserRepository(
            user = User(
                uid = "preview_user_123",
                fullName = "Ali Sarı",
                email = "ali.sari@rovits.com",
                photoUrl = null,
                isPasswordProvider = true,
                isAnonymous = false
            )
        )
        val viewModel = ProfileDetailViewModel(fakeRepo)
        ProfileDetailScreen(
            navController = rememberNavController(),
            viewModel = viewModel,
            onNavigateBack = {},
            onNavigateToChangePassword = {}
        )
    }
}

@Preview(showBackground = true, name = "Google User")
@Composable
fun ProfileDetailGoogleUserPreview() {
    RovitsAppTheme {
        val fakeRepo = FakeUserRepository(
            user = User(
                uid = "google_user_456",
                fullName = "Ayşe Yılmaz",
                email = "ayse.yilmaz@gmail.com",
                photoUrl = null,
                isPasswordProvider = false,
                isAnonymous = false
            )
        )
        val viewModel = ProfileDetailViewModel(fakeRepo)
        ProfileDetailScreen(
            navController = rememberNavController(),
            viewModel = viewModel,
            onNavigateBack = {},
            onNavigateToChangePassword = {}
        )
    }
}
