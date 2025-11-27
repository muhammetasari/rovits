package com.rovits.app.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rovits.app.R
import com.rovits.app.navigation.Screen
import com.rovits.app.ui.theme.RovitsAppTheme

@Composable
fun HomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var lastBackPressTime by remember { mutableStateOf(0L) }
    var showExitDialog by remember { mutableStateOf(false) }
    val exitTitle = stringResource(id = R.string.exit_app_title)
    val exitMessage = stringResource(id = R.string.exit_app_message)
    val yesText = stringResource(id = R.string.yes)
    val noText = stringResource(id = R.string.no)

    // Geri tuşu yakalama
    BackHandler(enabled = true) {
        val now = System.currentTimeMillis()
        if (now - lastBackPressTime < 500) {
            showExitDialog = true
        } else {
            lastBackPressTime = now
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(exitTitle) },
            text = { Text(exitMessage) },
            confirmButton = {
                TextButton(onClick = { showExitDialog = false; (context as? android.app.Activity)?.finish() }) {
                    Text(yesText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text(noText)
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        // 1. Üst Kısım (App Bar)
        topBar = {

        },
        // 2. Alt Kısım (Bottom Bar)
        bottomBar = {
            CustomBottomBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Content()
        }
    }
}


// --- Bottom Bar ---
@Composable
fun CustomBottomBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp) // Görseldeki gibi biraz yüksek
    ) {
        val navItems = listOf(
            Icons.Outlined.Home,
            Icons.Outlined.FavoriteBorder,
            Icons.Outlined.Explore, // Pusula yerine
            Icons.Outlined.CalendarToday,
            Icons.Outlined.Person
        )

        // Sadece görsel amaçlı state, Home seçili varsayalım
        var selectedIndex by remember { mutableIntStateOf(0) }

        navItems.forEachIndexed { index, icon ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    when (index) {
                        0 -> {
                            // TODO: Home sayfasına yönlendirme
                        }
                        1 -> {
                            // TODO: Favoriler sayfasına yönlendirme
                        }
                        2 -> {
                            // TODO: Keşfet sayfasına yönlendirme
                        }
                        3 -> {
                            // TODO: Takvim sayfasına yönlendirme
                        }
                        4 -> {
                            navController.navigate(Screen.Profile.route)
                        }
                    }
                },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        // Seçili ise alttaki yeşil çizgi
                        if (selectedIndex == index) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}



@Composable
fun Content(){

}

@Preview(showBackground = true)
@Composable
fun HomeScreenDesignPreview() {
    RovitsAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}
