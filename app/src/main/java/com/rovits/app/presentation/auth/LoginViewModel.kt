package com.rovits.app.presentation.auth

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.rovits.app.R
import com.rovits.app.data.auth.GoogleAuthManager
import com.rovits.app.data.repository.AuthRepository
import com.rovits.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleAuthManager: GoogleAuthManager,
    @ApplicationContext private val context: android.content.Context
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginState.value = LoginState.Loading
                    }
                    is Resource.Success -> {
                        _loginState.value = LoginState.Success(result.data ?: "")
                    }
                    is Resource.Error -> {
                        _loginState.value = LoginState.Error(result.message ?: context.getString(R.string.error_unknown))
                    }
                }
            }
        }
    }

    private fun socialLogin(firebaseToken: String) {
        viewModelScope.launch {
            authRepository.socialLogin(firebaseToken).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginState.value = LoginState.Loading
                    }
                    is Resource.Success -> {
                        _loginState.value = LoginState.Success(result.data ?: "")
                    }
                    is Resource.Error -> {
                        _loginState.value = LoginState.Error(result.message ?: context.getString(R.string.error_unknown))
                    }
                }
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            val googleApiAvailability = GoogleApiAvailability.getInstance()
            val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)

            if (resultCode != ConnectionResult.SUCCESS) {
                _loginState.value = LoginState.Error(context.getString(R.string.error_google_play_services))
                return@launch
            }

            try {
                val intentSender = googleAuthManager.signIn()
                if (intentSender != null) {
                    _loginState.value = LoginState.GoogleSignInIntentReady(intentSender)
                } else {
                    _loginState.value = LoginState.Error(context.getString(R.string.error_google_sign_in_intent))
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: context.getString(R.string.error_google_sign_in_failed))
            }
        }
    }

    fun handleGoogleSignInResult(intent: Intent) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val firebaseToken = googleAuthManager.signInWithIntent(intent)
                if (firebaseToken != null) {
                    // Firebase token ile kendi backend'imize socialLogin isteği atıyoruz
                    socialLogin(firebaseToken)
                } else {
                    _loginState.value = LoginState.Error(context.getString(R.string.error_google_firebase_token))
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: context.getString(R.string.error_google_sign_in_handle))
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

// State'leri tek bir sealed class altında birleştirdik.
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
    data class GoogleSignInIntentReady(val intentSender: IntentSender) : LoginState()
}