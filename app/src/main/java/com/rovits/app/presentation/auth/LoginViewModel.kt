package com.rovits.app.presentation.auth

import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val googleAuthManager: GoogleAuthManager,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: android.content.Context
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    /**
     * Login with email and password via Firebase, then authenticate with backend
     */
    fun loginWithEmailPassword(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                // 1. Firebase Authentication ile giriş yap
                val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()

                // 2. Firebase ID token al
                val firebaseToken = authResult.user?.getIdToken(false)?.await()?.token

                if (firebaseToken != null) {
                    // 3. Backend'e firebaseToken ile login yap
                    login(firebaseToken)
                } else {
                    _loginState.value = LoginState.Error(context.getString(R.string.error_invalid_token))
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(
                    e.localizedMessage ?: context.getString(R.string.error_unknown)
                )
            }
        }
    }

    /**
     * Login with Firebase ID token (unified login for email/password and social)
     */
    fun login(firebaseToken: String) {
        viewModelScope.launch {
            authRepository.login(firebaseToken).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginState.value = LoginState.Loading
                    }
                    is Resource.Success -> {
                        _loginState.value = LoginState.Success(result.data ?: "")
                    }
                    is Resource.Error -> {
                        // Error code kontrolü (öncelikli)
                        android.util.Log.d("LoginViewModel", "Login error - code: ${result.errorCode}, message: ${result.message}")
                        if (result.errorCode == "AUTH_009") {
                            _loginState.value = LoginState.EmailNotVerified(firebaseToken)
                        } else {
                            // Fallback: Message içinde kontrol (geriye dönük uyumluluk)
                            val errorMessage = result.message ?: context.getString(R.string.error_unknown)
                            if (errorMessage.contains(context.getString(R.string.error_email_not_verified), ignoreCase = true)) {
                                _loginState.value = LoginState.EmailNotVerified(firebaseToken)
                            } else {
                                _loginState.value = LoginState.Error(errorMessage)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Send email verification using Firebase SDK directly
     */
    fun sendEmailVerification(firebaseToken: String) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                val currentUser = firebaseAuth.currentUser

                if (currentUser != null) {
                    // Firebase SDK ile doğrulama e-postası gönder
                    currentUser.sendEmailVerification().await()
                    Log.i("LoginViewModel", "Email verification sent via Firebase SDK")
                    _loginState.value = LoginState.VerificationEmailSent(firebaseToken)
                } else {
                    Log.e("LoginViewModel", "No current user, cannot send verification email")
                    _loginState.value = LoginState.Error(context.getString(R.string.error_user_not_logged_in))
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Error sending verification email", e)
                _loginState.value = LoginState.Error(
                    e.localizedMessage ?: context.getString(R.string.error_send_verification_email)
                )
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
                val request = googleAuthManager.signIn()
                val credentialManager = googleAuthManager.getCredentialManager()
                _loginState.value = LoginState.GoogleSignInRequestReady(credentialManager, request)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: context.getString(R.string.error_google_sign_in_failed))
            }
        }
    }

    fun handleGoogleSignInResult(credential: androidx.credentials.Credential) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val firebaseToken = googleAuthManager.signInWithCredential(credential)
                if (firebaseToken != null) {
                    // Firebase token ile kendi backend'imize login isteği atıyoruz (unified login)
                    login(firebaseToken)
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

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
    data class EmailNotVerified(val firebaseToken: String) : LoginState()
    data class VerificationEmailSent(val firebaseToken: String) : LoginState()
    data class GoogleSignInRequestReady(val credentialManager: CredentialManager, val request: GetCredentialRequest) : LoginState()
}