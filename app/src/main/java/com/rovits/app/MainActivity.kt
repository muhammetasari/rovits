package com.rovits.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rovits.app.ui.screens.ForgotPasswordScreen
import com.rovits.app.ui.screens.LoginScreen
import com.rovits.app.ui.screens.RegisterScreen
import com.rovits.app.ui.screens.SplashScreen
import com.rovits.app.ui.theme.RovitsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RovitsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RovitsNavigation()
                }
            }
        }
    }
}

@Composable
fun RovitsNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginClick = { email, password ->
                    // TODO: Implement login logic
                    Toast.makeText(context, "Login: $email", Toast.LENGTH_SHORT).show()
                },
                onGoogleSignInClick = {
                    // TODO: Implement Google sign in
                    Toast.makeText(context, "Google Sign In", Toast.LENGTH_SHORT).show()
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                onSendResetLink = { email ->
                    // TODO: Implement password reset
                    Toast.makeText(context, "Reset link sent to: $email", Toast.LENGTH_SHORT).show()
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onBackPressed = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = { fullName, email, password ->
                    // TODO: Implement registration logic
                    Toast.makeText(context, "Register: $fullName, $email", Toast.LENGTH_SHORT).show()
                },
                onGoogleSignInClick = {
                    // TODO: Implement Google sign in
                    Toast.makeText(context, "Google Sign In", Toast.LENGTH_SHORT).show()
                },
                onLanguageClick = {
                    // TODO: Implement language change
                    Toast.makeText(context, "Language Change", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

