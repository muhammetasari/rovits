package com.rovits.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.rovits.app.ui.screens.authscreen.ForgotPasswordScreen
import com.rovits.app.ui.screens.HomeScreen
import com.rovits.app.ui.screens.authscreen.LoginScreen
import com.rovits.app.ui.screens.authscreen.RegisterScreen
import com.rovits.app.ui.screens.SplashScreen
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.viewmodel.AuthViewModel
import com.rovits.app.utils.LocaleHelper
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { LocaleHelper.setLocale(it, LocaleHelper.getPersistedLocale(it)) })
    }

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
    val viewModel: AuthViewModel = viewModel()
    val authState by viewModel.authState.collectAsState()

    // Google Identity Services (GIS) One Tap Client ve SignInRequest
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val signInRequest = remember {
        BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    // GIS One Tap için launcher
    val oneTapLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    viewModel.signInWithGoogle(idToken, context)
                } else {
                    android.util.Log.e("GoogleSignIn", "ID Token is null")
                }
            } catch (e: Exception) {
                android.util.Log.e("GoogleSignIn", "Error getting credential", e)
            }
        } else {
            android.util.Log.e("GoogleSignIn", "Sign-In cancelled or failed. Result code: ${result.resultCode}")
        }
    }

    // Check authentication state for navigation
    LaunchedEffect(authState.currentUser) {
        if (authState.currentUser != null && navController.currentDestination?.route == "splash") {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

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
                viewModel = viewModel,
                onNavigateToForgotPassword = {
                    navController.navigate("forgot_password")
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener { result: BeginSignInResult ->
                            android.util.Log.d("GoogleSignIn", "Sign-In request successful")
                            oneTapLauncher.launch(
                                androidx.activity.result.IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                            )
                        }
                        .addOnFailureListener { e ->
                            android.util.Log.e("GoogleSignIn", "Sign-In request failed", e)
                        }
                }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                },
                onResetSuccess = {
                    navController.navigateUp()
                }
            )
        }

        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onBackPressed = {
                    navController.navigateUp()
                },
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    oneTapClient.beginSignIn(signInRequest)
                        .addOnSuccessListener { result: BeginSignInResult ->
                            android.util.Log.d("GoogleSignIn", "Sign-In request successful (Register)")
                            oneTapLauncher.launch(
                                androidx.activity.result.IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                            )
                        }
                        .addOnFailureListener { e ->
                            android.util.Log.e("GoogleSignIn", "Sign-In request failed (Register)", e)
                        }
                }
            )
        }

        composable("home") {
            HomeScreen(
                user = authState.currentUser,
                onLogout = {
                    oneTapClient.signOut().addOnCompleteListener {
                        viewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
