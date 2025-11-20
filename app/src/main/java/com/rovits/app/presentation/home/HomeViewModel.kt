package com.rovits.app.presentation.home

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
import android.util.Log

sealed class LogoutState {
    object Idle : LogoutState()
    object Loading : LogoutState()
    object Success : LogoutState()
    data class Error(val message: String) : LogoutState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState: StateFlow<LogoutState> = _logoutState.asStateFlow()

    /**
     * Logout user from backend and clear local data
     */
    fun logout() {
        viewModelScope.launch {
            try {
                _logoutState.value = LogoutState.Loading
                Log.d(TAG, "Starting logout...")

                authRepository.logout().collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _logoutState.value = LogoutState.Loading
                        }
                        is Resource.Success -> {
                            Log.i(TAG, "Logout successful")
                            _logoutState.value = LogoutState.Success
                        }
                        is Resource.Error -> {
                            // Logout hata verirse bile success sayıyoruz
                            // Çünkü local data temizlendi
                            Log.w(TAG, "Logout warning: ${result.message}")
                            _logoutState.value = LogoutState.Success
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Logout error", e)
                // Exception olsa bile success sayıyoruz
                _logoutState.value = LogoutState.Success
            }
        }
    }

    /**
     * Reset logout state after navigation
     */
    fun resetLogoutState() {
        _logoutState.value = LogoutState.Idle
    }
}