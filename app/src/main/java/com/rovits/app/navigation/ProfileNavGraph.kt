package com.rovits.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rovits.app.data.model.User
import com.rovits.app.ui.screens.ChangePasswordScreen
import com.rovits.app.ui.screens.ProfileDetailScreen
import com.rovits.app.ui.screens.ProfileScreen
import com.rovits.app.ui.screens.SettingsScreen
import com.rovits.app.ui.theme.demo.ThemeDemoScreen
import com.rovits.app.ui.viewmodel.AuthViewModel



fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    user: User?,
    onLogout: () -> Unit,
    authViewModel: AuthViewModel
) {
    composable(Screen.Profile.route) {
        ProfileScreen(
            navController = navController,
            user = user,
            onLogout = onLogout,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToThemeDemo = { navController.navigate(Screen.ThemeDemo.route) },
            onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
            onNavigateToProfileDetail = { navController.navigate(Screen.ProfileDetail.route) }
        )
    }

    composable(Screen.ProfileDetail.route) {
        ProfileDetailScreen(
            navController = navController,
            user = user,
            onNavigateBack = { navController.popBackStack() },
            onNavigateToChangePassword = { navController.navigate(Screen.ChangePassword.route) }
        )
    }

    composable(Screen.ThemeDemo.route) {
        ThemeDemoScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }

    composable(Screen.Settings.route) {
        SettingsScreen(
            navController = navController
        )
    }

    composable(Screen.ChangePassword.route) {
        ChangePasswordScreen(
            navController = navController,
            viewModel = authViewModel,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
