package com.rovits.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.*
import androidx.core.os.LocaleListCompat
import androidx.navigation.compose.rememberNavController
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.navigation.NavGraph
import com.rovits.app.navigation.Screen
import com.rovits.app.presentation.splash.SplashScreen
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.util.JwtValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.rovits.app.presentation.settings.LocaleViewModel
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val localeViewModel: LocaleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Set the app locale on startup
            LaunchedEffect(Unit) {
                val savedLanguage = localeViewModel.getSavedLanguage()
                if (!savedLanguage.isNullOrEmpty()) {
                    val locale = Locale.forLanguageTag(savedLanguage)
                    val appLocale = LocaleListCompat.create(locale)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
            }

            RovitsAppTheme {
                val navController = rememberNavController()
                var startDestination by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(Unit) {
                    val token = preferencesManager.getJwtToken().first()
                    val isTokenValid = JwtValidator.isTokenValid(token)

                    if (isTokenValid) {
                        startDestination = Screen.Home.route
                    } else {
                        if (!token.isNullOrEmpty()) {
                            // Token exists but is invalid, clear it
                            preferencesManager.clearAll()
                        }
                        startDestination = Screen.Login.route
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