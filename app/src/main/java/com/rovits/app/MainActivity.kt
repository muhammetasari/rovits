package com.rovits.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.navigation.NavGraph
import com.rovits.app.navigation.Screen
import com.rovits.app.presentation.splash.SplashScreen
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.util.JwtValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RovitsAppTheme {
                val navController = rememberNavController()

                // JWT token'ı collect et
                val jwtToken by preferencesManager.getJwtToken()
                    .collectAsStateWithLifecycle(initialValue = null)

                // Loading state - Token okunana kadar splash screen göster
                var isLoading by remember { mutableStateOf(true) }
                var isTokenValid by remember { mutableStateOf(false) }

                // Token kontrolü ve validation
                LaunchedEffect(jwtToken) {
                    // Minimum splash screen süresi (UX için)
                    delay(500)

                    // Token'ı validate et
                    isTokenValid = JwtValidator.isTokenValid(jwtToken)

                    // Token geçersizse temizle
                    if (!jwtToken.isNullOrEmpty() && !isTokenValid) {
                        preferencesManager.clearAll()
                    }

                    isLoading = false
                }

                // Token durumuna göre start destination belirle
                val startDestination by remember {
                    derivedStateOf {
                        if (isTokenValid && !jwtToken.isNullOrEmpty()) {
                            Screen.Home.route
                        } else {
                            Screen.Login.route
                        }
                    }
                }

                // Token değişikliklerini dinle ve navigate et
                LaunchedEffect(jwtToken, isTokenValid) {
                    if (!isLoading) {
                        if (jwtToken.isNullOrEmpty() || !isTokenValid) {
                            // Token yoksa veya geçersizse Login'e yönlendir
                            if (navController.currentDestination?.route != Screen.Login.route) {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        } else {
                            // Token varsa ve geçerliyse Home'a yönlendir
                            if (navController.currentDestination?.route != Screen.Home.route) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    }
                }

                // Splash screen göster veya ana içeriği göster
                if (isLoading) {
                    SplashScreen()
                } else {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        NavGraph(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}