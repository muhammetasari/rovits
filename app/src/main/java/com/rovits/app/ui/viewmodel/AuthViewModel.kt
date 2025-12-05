package com.rovits.app.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.R
import com.rovits.app.data.model.AuthResult
import com.rovits.app.data.model.AuthState
import com.rovits.app.data.repository.AuthRepository
import com.rovits.app.util.error.ErrorMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        viewModelScope.launch {
            try {
                val user = repository.getCurrentUser()
                _authState.value = _authState.value.copy(
                    currentUser = user,
                    error = null
                )
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Failed to check current user", e)
                _authState.value = _authState.value.copy(
                    currentUser = null,
                    error = e.message
                )
            }
        }
    }

    fun signInWithEmail(email: String, password: String, context: Context) {
        // Validation
        if (email.isBlank()) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_email_empty)
            )
            return
        }

        if (password.isBlank()) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_password_empty)
            )
            return
        }

        if (!isValidEmail(email)) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_invalid_email_format)
            )
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null, isSuccess = false)

            when (val result = repository.signInWithEmail(email, password)) {
                is AuthResult.Success -> {
                    _authState.value = AuthState(
                        isLoading = false,
                        currentUser = result.data,
                        error = null,
                        isSuccess = true
                    )
                }
                is AuthResult.Error -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = ErrorMapper.mapToMessage(context, result.exception),
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun signUpWithEmail(fullName: String, email: String, password: String, context: Context) {
        // Validation
        if (fullName.isBlank()) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_full_name_required)
            )
            return
        }

        if (email.isBlank()) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_email_empty)
            )
            return
        }

        if (password.isBlank()) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_password_empty)
            )
            return
        }

        if (!isValidEmail(email)) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_invalid_email_format)
            )
            return
        }

        if (password.length < 6) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_password_too_short)
            )
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null, isSuccess = false)

            when (val result = repository.signUpWithEmail(fullName, email, password)) {
                is AuthResult.Success -> {
                    _authState.value = AuthState(
                        isLoading = false,
                        currentUser = result.data,
                        error = null,
                        isSuccess = true
                    )
                }
                is AuthResult.Error -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = ErrorMapper.mapToMessage(context, result.exception),
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun signInWithGoogle(idToken: String, context: Context) {
        android.util.Log.d("AuthViewModel", "signInWithGoogle called with idToken: ${idToken.take(20)}...")
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null, isSuccess = false)

            when (val result = repository.signInWithGoogle(idToken)) {
                is AuthResult.Success -> {
                    android.util.Log.d("AuthViewModel", "Google Sign-In successful: ${result.data.email}")
                    _authState.value = AuthState(
                        isLoading = false,
                        currentUser = result.data,
                        error = null,
                        isSuccess = true
                    )
                }
                is AuthResult.Error -> {
                    android.util.Log.e("AuthViewModel", "Google Sign-In failed: ${result.exception.message}")
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = ErrorMapper.mapToMessage(context, result.exception),
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun sendPasswordResetEmail(email: String, context: Context) {
        if (email.isBlank()) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_email_empty)
            )
            return
        }

        if (!isValidEmail(email)) {
            _authState.value = _authState.value.copy(
                error = context.getString(R.string.error_invalid_email_format)
            )
            return
        }

        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null, isSuccess = false)

            when (val result = repository.sendPasswordResetEmail(email)) {
                is AuthResult.Success -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = null,
                        isSuccess = true
                    )
                }
                is AuthResult.Error -> {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = ErrorMapper.mapToMessage(context, result.exception),
                        isSuccess = false
                    )
                }
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState()
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }

    fun clearSuccess() {
        _authState.value = _authState.value.copy(isSuccess = false)
    }

    fun setGoogleSignInError(message: String) {
        _authState.value = _authState.value.copy(googleSignInError = message)
    }

    fun clearGoogleSignInError() {
        _authState.value = _authState.value.copy(googleSignInError = null)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
