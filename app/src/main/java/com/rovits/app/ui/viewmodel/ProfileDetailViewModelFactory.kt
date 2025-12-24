package com.rovits.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rovits.app.data.repository.IUserRepository

class ProfileDetailViewModelFactory(
    private val userRepository: IUserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileDetailViewModel::class.java)) {
            return ProfileDetailViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

