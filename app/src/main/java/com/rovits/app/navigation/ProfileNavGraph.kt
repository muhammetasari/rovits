package com.rovits.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rovits.app.data.model.User
import com.rovits.app.ui.screens.ProfileScreen

fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    user: User?,
    onLogout: () -> Unit
) {
    composable(Screen.Profile.route) {
        ProfileScreen(
            user = user,
            onLogout = onLogout,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
