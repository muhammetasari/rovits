package com.rovits.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rovits.app.presentation.auth.LoginScreen
import com.rovits.app.presentation.auth.RegisterScreen
import com.rovits.app.presentation.home.HomeScreen
import com.rovits.app.presentation.settings.LanguageScreen // YENİ

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Register Screen
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                // YENİ
                onNavigateToLanguage = {
                    navController.navigate(Screen.Language.route)
                }
            )
        }

        // Language Screen - YENİ
        composable(Screen.Language.route) {
            LanguageScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}