package com.rovits.app.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StandardBottomBar(navController: NavController) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Ana Sayfa", "Arama", "Randevu", "Profil", "Ayarlar")

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    // TODO: İkonları projenize uygun olanlarla değiştirin
                    when (index) {
                        0 -> Icon(Icons.Default.Home, contentDescription = item)
                        1 -> Icon(Icons.Default.Search, contentDescription = item)
                        2 -> Icon(Icons.Default.DateRange, contentDescription = item)
                        3 -> Icon(Icons.Default.AccountCircle, contentDescription = item)
                        4 -> Icon(Icons.Default.Settings, contentDescription = item)
                    }
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    // TODO: Buraya yönlendirme mantığını ekleyin
                    /*
                    when(index) {
                        0 -> navController.navigate("home_route") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                        1 -> navController.navigate("search_route") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                        2 -> navController.navigate("appointment_route") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                        3 -> navController.navigate("profile_route") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                        4 -> navController.navigate("settings_route") { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true }
                    }
                    */
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StandardBottomBarPreview() {
    StandardBottomBar(navController = rememberNavController())
}
