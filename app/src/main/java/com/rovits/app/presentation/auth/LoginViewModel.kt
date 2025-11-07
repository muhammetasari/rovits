package com.rovits.app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val authRepository: AuthRepository
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
                        _loginState.value = LoginState.Error(result.message ?: "Unknown error")
                    }
                }
            }
        }
    }

    fun socialLogin(firebaseToken: String, provider: String) {
        viewModelScope.launch {
            authRepository.socialLogin(firebaseToken, provider).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loginState.value = LoginState.Loading
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

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: String) : LoginState()
    data class Error(val message: String) : LoginState()
}