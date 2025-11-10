package com.rovits.app.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Language : Screen("language")
}