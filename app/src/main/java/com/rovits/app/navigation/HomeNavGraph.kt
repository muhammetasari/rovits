package com.rovits.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rovits.app.ui.screens.HomeScreen

fun NavGraphBuilder.homeNavGraph(navController: NavController) {
    composable(Screen.Home.route) {
        HomeScreen(navController = navController)
    }
}

