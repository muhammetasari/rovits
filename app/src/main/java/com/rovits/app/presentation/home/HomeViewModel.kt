package com.rovits.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _logoutCompleted = MutableStateFlow(false)
    val logoutCompleted: StateFlow<Boolean> = _logoutCompleted.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _logoutCompleted.value = true
        }
    }

    fun onLogoutCompleted() {
        _logoutCompleted.value = false
    }
}