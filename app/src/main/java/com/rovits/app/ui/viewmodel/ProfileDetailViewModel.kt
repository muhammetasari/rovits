package com.rovits.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.model.User
import com.rovits.app.data.repository.IUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Profil detay ekranı için ViewModel.
 * Kullanıcı verisini ve UI durumunu yönetir.
 */
class ProfileDetailViewModel(
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileDetailUiState>(ProfileDetailUiState.Loading)
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    init {
        userRepository.getCurrentUser()
            .onEach { user ->
                _uiState.value = ProfileDetailUiState.Success(user)
            }
            .catch { e ->
                _uiState.value = ProfileDetailUiState.Error(e.message ?: "Bilinmeyen hata")
            }
            .launchIn(viewModelScope)
    }
}

sealed class ProfileDetailUiState {
    object Loading : ProfileDetailUiState()
    data class Success(val user: User?) : ProfileDetailUiState()
    data class Error(val message: String) : ProfileDetailUiState()
}

