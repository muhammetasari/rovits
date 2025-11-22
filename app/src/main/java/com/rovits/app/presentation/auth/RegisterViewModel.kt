package com.rovits.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.rovits.app.R
import com.rovits.app.data.repository.AuthRepository
import com.rovits.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

// 1. State Sınıfı
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val token: String) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

// 2. ViewModel
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    /**
     * Register with email and password via Firebase, then authenticate with backend
     */
    fun registerWithEmailPassword(name: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                // 1. Firebase Authentication ile kullanıcı oluştur
                val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

                // 2. Kullanıcı profiline isim ekle
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                authResult.user?.updateProfile(profileUpdates)?.await()

                // 3. Firebase ID token al
                val firebaseToken = authResult.user?.getIdToken(false)?.await()?.token

                if (firebaseToken != null) {
                    // 4. Backend'e firebaseToken ile register yap
                    register(firebaseToken)
                } else {
                    _registerState.value = RegisterState.Error(context.getString(R.string.error_invalid_token))
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(
                    e.localizedMessage ?: context.getString(R.string.error_unknown)
                )
            }
        }
    }

    /**
     * Register with Firebase ID token
     * Client önce Firebase'de kullanıcı oluşturmalı, ardından ID token'ı göndermelidir
     */
    private fun register(firebaseToken: String) {
        viewModelScope.launch {
            authRepository.register(firebaseToken).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _registerState.value = RegisterState.Loading
                    }
                    is Resource.Success -> {
                        _registerState.value = RegisterState.Success(result.data ?: "")
                    }
                    is Resource.Error -> {
                        _registerState.value = RegisterState.Error(result.message ?: context.getString(R.string.error_unknown))
                    }
                }
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}
