package com.rovits.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rovits.app.presentation.auth.LoginScreen
import com.rovits.app.presentation.auth.RegisterScreen // YENİ IMPORT
import com.rovits.app.presentation.home.HomeScreen

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
                // YENİ EKLENEN PARAMETRE
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // YENİ EKLENEN COMPOSABLE
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    // Kayıt olunca da ana ekrana git
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack() // Geri dön
                }
            )
        }

        // Home Screen (placeholder)
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}