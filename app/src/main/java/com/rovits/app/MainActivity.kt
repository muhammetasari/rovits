package com.rovits.app

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.navigation.NavGraph
import com.rovits.app.navigation.Screen
import com.rovits.app.presentation.splash.SplashScreen
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.util.JwtValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import com.rovits.app.presentation.settings.LocaleViewModel
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val localeViewModel: LocaleViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val locale = runBlocking(Dispatchers.Default) {
            try {
                LocaleViewModel.getSavedLocale(newBase)
            } catch (e: Exception) {
                Locale.getDefault()
            }
        }
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                localeViewModel.recreateActivity
                    .flowWithLifecycle(lifecycle)
                    .collect {
                        recreate()
                    }
            }

            RovitsAppTheme {
                val navController = rememberNavController()

                val jwtToken by preferencesManager.getJwtToken()
                    .collectAsStateWithLifecycle(initialValue = null)

                var isLoading by remember { mutableStateOf(true) }
                var isTokenValid by remember { mutableStateOf(false) }

                // Token kontrolü
                LaunchedEffect(jwtToken) {
                    delay(500)
                    isTokenValid = JwtValidator.isTokenValid(jwtToken)

                    if (!jwtToken.isNullOrEmpty() && !isTokenValid) {
                        preferencesManager.clearAll()
                    }

                    isLoading = false
                }

                // Start destination belirleme
                val startDestination = remember(isLoading, isTokenValid, jwtToken) {
                    if (isLoading) return@remember Screen.Login.route
                    if (isTokenValid && !jwtToken.isNullOrEmpty()) {
                        Screen.Home.route
                    } else {
                        Screen.Login.route
                    }
                }

                // UI
                if (isLoading) {
                    SplashScreen()
                } else {
                    NavGraph(
                        navController = navController,
                        startDestination = startDestination
                    )
                }
            }
        }
    }
}