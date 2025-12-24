package com.rovits.app.data.repository

import com.rovits.app.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Kullanıcı verisi için repository interface'i.
 */
interface IUserRepository {
    fun getCurrentUser(): Flow<User?>
}
