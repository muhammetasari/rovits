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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.rovits.app.navigation.Screen
import com.rovits.app.navigation.authNavGraph
import com.rovits.app.navigation.homeNavGraph
import com.rovits.app.navigation.profileNavGraph
import com.rovits.app.ui.theme.RovitsAppTheme
import com.rovits.app.ui.viewmodel.AuthViewModel
import com.rovits.app.utils.LocaleHelper
import androidx.compose.runtime.remember

private const val GOOGLE_SIGN_IN_TAG = "GoogleSignIn"

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
    val webClientId = remember { context.getString(R.string.default_web_client_id) }

    val signInRequest = remember(webClientId) {
        BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
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
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = credential.googleIdToken

                    if (idToken != null) {
                        viewModel.signInWithGoogle(idToken, context)
                    } else {
                        android.util.Log.e(GOOGLE_SIGN_IN_TAG, "ID Token is null")
                        viewModel.setGoogleSignInError(context.getString(R.string.error_google_sign_in_token))
                    }
                } catch (e: Exception) {
                    android.util.Log.e(GOOGLE_SIGN_IN_TAG, "Error getting credential: ${e.message}", e)
                    viewModel.setGoogleSignInError(context.getString(R.string.error_google_sign_in_credential))
                }
            }
            Activity.RESULT_CANCELED -> {
                android.util.Log.d(GOOGLE_SIGN_IN_TAG, "Sign-In cancelled by user")
                viewModel.setGoogleSignInError(context.getString(R.string.error_google_sign_in_cancelled))
            }
            else -> {
                android.util.Log.e(GOOGLE_SIGN_IN_TAG, "Sign-In failed with result code: ${result.resultCode}")
                viewModel.setGoogleSignInError(context.getString(R.string.error_google_sign_in_failed))
            }
        }
    }


    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        authNavGraph(
            navController = navController,
            viewModel = viewModel,
            oneTapClient = oneTapClient,
            signInRequest = signInRequest,
            oneTapLauncher = oneTapLauncher
        )
        homeNavGraph(navController = navController)
        profileNavGraph(
            navController = navController,
            user = authState.currentUser,
            onLogout = {
                viewModel.signOut()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            }
        )
    }
}
