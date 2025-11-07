package com.rovits.app.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object PlaceDetails : Screen("place_details/{placeId}") {
        fun createRoute(placeId: String) = "place_details/$placeId"
    }
}