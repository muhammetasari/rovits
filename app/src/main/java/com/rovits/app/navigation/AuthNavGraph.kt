package com.rovits.app.navigation

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.rovits.app.ui.screens.SplashScreen
import com.rovits.app.ui.screens.authscreen.ForgotPasswordScreen
import com.rovits.app.ui.screens.authscreen.LoginScreen
import com.rovits.app.ui.screens.authscreen.RegisterScreen
import com.rovits.app.ui.viewmodel.AuthViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    viewModel: AuthViewModel,
    oneTapClient: SignInClient,
    signInRequest: BeginSignInRequest,
    oneTapLauncher: ActivityResultLauncher<IntentSenderRequest>
) {
    composable(Screen.Splash.route) {
        val authState by viewModel.authState.collectAsState()

        SplashScreen(
            onNavigateToLogin = {
                if (authState.currentUser != null) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        )
    }
    composable(Screen.Login.route) {
        LoginScreen(
            viewModel = viewModel,
            onNavigateToForgotPassword = {
                navController.navigate(Screen.ForgotPassword.route)
            },
            onNavigateToRegister = {
                navController.navigate(Screen.Register.route)
            },
            onLoginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onGoogleSignInClick = {
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener { result ->
                        oneTapLauncher.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        )
                    }
                    .addOnFailureListener { }
            }
        )
    }
    composable(Screen.ForgotPassword.route) {
        ForgotPasswordScreen(
            viewModel = viewModel,
            onBackPressed = { navController.navigateUp() },
            onResetSuccess = { navController.navigateUp() }
        )
    }
    composable(Screen.Register.route) {
        RegisterScreen(
            viewModel = viewModel,
            onBackPressed = { navController.navigateUp() },
            onNavigateToLogin = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onRegisterSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Register.route) { inclusive = true }
                }
            },
            onGoogleSignInClick = {
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener { result ->
                        oneTapLauncher.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        )
                    }
                    .addOnFailureListener { }
            }
        )
    }
}
