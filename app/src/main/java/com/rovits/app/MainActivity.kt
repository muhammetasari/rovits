package com.rovits.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.navigation.NavGraph
import com.rovits.app.navigation.Screen
import com.rovits.app.ui.theme.RovitsAppTheme
import dagger.hilt.android.AndroidEntryPoint
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

                // JWT token varsa Home'a, yoksa Login'e yönlendir
                val jwtToken by preferencesManager.getJwtToken()
                    .collectAsStateWithLifecycle(initialValue = null)

                val startDestination = remember(jwtToken) {
                    if (jwtToken.isNullOrEmpty()) {
                        Screen.Login.route
                    } else {
                        Screen.Home.route
                    }
                }

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