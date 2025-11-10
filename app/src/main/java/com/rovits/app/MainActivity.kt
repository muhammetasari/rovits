package com.rovits.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.navigation.NavGraph
import com.rovits.app.navigation.Screen
import com.rovits.app.presentation.splash.SplashScreen
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.util.JwtValidator
import com.rovits.app.util.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun attachBaseContext(newBase: Context) {
        // LocaleHelper kullan (Hilt henüz çalışmıyor bu aşamada)
        // LocaleHelper ve PreferencesManager senkronize tutulacak
        super.attachBaseContext(LocaleHelper.setLocale(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RovitsAppTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    try {
                        val token = preferencesManager.getJwtToken().first()
                        val isTokenValid = JwtValidator.isTokenValid(token)


                        startDestination = if (isTokenValid) {
                            Screen.Home.route
                        } else {
                            if (!token.isNullOrEmpty()) {
                                preferencesManager.clearAll()
                            }
                            Screen.Login.route
                        }
                    } catch (_: Exception) {
                        // Herhangi bir hata durumunda login ekranına yönlendir
                        startDestination = Screen.Login.route
                    }
                }

                when (startDestination) {
                    null -> SplashScreen()
                    else -> NavGraph(
                        navController = navController,
                        startDestination = startDestination!!
                    )
                }
            }
        }
    }
}