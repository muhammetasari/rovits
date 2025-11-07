package com.rovits.app.presentation.auth

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.auth.GoogleAuthManager
import com.rovits.app.data.repository.AuthRepository
import com.rovits.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleAuthManager: GoogleAuthManager
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _googleSignInState = MutableStateFlow<GoogleSignInState>(GoogleSignInState.Idle)
    val googleSignInState: StateFlow<GoogleSignInState> = _googleSignInState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginState.value = LoginState.Loading
                        _googleSignInState.value = GoogleSignInState.Idle // Diğer state'i sıfırla
                    }
                    is Resource.Success -> {
                        _loginState.value = LoginState.Success(result.data ?: "")
                    }
                    is Resource.Error -> {
                        _loginState.value = LoginState.Error(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    private fun socialLogin(firebaseToken: String, provider: String) {
        viewModelScope.launch {
            authRepository.socialLogin(firebaseToken, provider).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginState.value = LoginState.Loading
                        _googleSignInState.value = GoogleSignInState.Idle
                    }
                    is Resource.Success -> {
                        _loginState.value = LoginState.Success(result.data ?: "")
                    }
                    is Resource.Error -> {
                        _loginState.value = LoginState.Error(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _googleSignInState.value = GoogleSignInState.Loading
            _loginState.value = LoginState.Idle // Diğer state'i sıfırla
            try {
                val intentSender = googleAuthManager.signIn()
                if (intentSender != null) {
                    _googleSignInState.value = GoogleSignInState.IntentReady(intentSender)
                } else {
                    _googleSignInState.value = GoogleSignInState.Error("Google Sign-In intent could not be created.")
                }
            } catch (e: Exception) {
                _googleSignInState.value = GoogleSignInState.Error(e.message ?: "Google Sign-In failed")
            }
        }
    }

    fun handleGoogleSignInResult(intent: Intent) {
        viewModelScope.launch {
            _googleSignInState.value = GoogleSignInState.Loading
            try {
                val firebaseToken = googleAuthManager.signInWithIntent(intent)
                if (firebaseToken != null) {
                    // Firebase token ile kendi backend'imize socialLogin isteği atıyoruz
                    socialLogin(firebaseToken, "google")
                } else {
                    _googleSignInState.value = GoogleSignInState.Error("Failed to get Firebase token from Google intent.")
                }
            } catch (e: Exception) {
                _googleSignInState.value = GoogleSignInState.Error(e.message ?: "Google Sign-In result handling failed")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
        _googleSignInState.value = GoogleSignInState.Idle
    }
}

// State'leri ViewModel'in dışına, ancak aynı dosyaya taşıdık.
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class GoogleSignInState {
    object Idle : GoogleSignInState()
    object Loading : GoogleSignInState()
    data class IntentReady(val intentSender: IntentSender) : GoogleSignInState()
    data class Error(val message: String) : GoogleSignInState()
}