package com.rovits.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.navigation.NavGraph
import com.rovits.app.navigation.Screen
import com.rovits.app.presentation.splash.SplashScreen
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.util.JwtValidator
import com.rovits.app.util.Language
import com.rovits.app.util.LocaleManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Kayıtlı dili uygula
        lifecycleScope.launch {
            val languageCode = preferencesManager.getLanguage().first()
            if (languageCode != null) {
                val language = Language.fromCode(languageCode)
                LocaleManager.setLocale(this@MainActivity, language)
            }
        }

        setContent {
            RovitsAppTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
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
                }

                if (startDestination != null) {
                    NavGraph(
                        navController = navController,
                        startDestination = startDestination!!
                    )
                } else {
                    SplashScreen()
                }
            }
        }
    }
}