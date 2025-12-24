package com.rovits.app.data.repository.fake

import com.rovits.app.data.model.User
import com.rovits.app.data.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Preview ve test için fake user repository.
 */
class FakeUserRepository(private val user: User? = MOCK_USER) : IUserRepository {
    override fun getCurrentUser(): Flow<User?> = flowOf(user)

    companion object {
        val MOCK_USER = User(
            uid = "preview_user_123",
            fullName = "Ali Sarı",
            email = "ali.sari@rovits.com",
            photoUrl = null,
            isPasswordProvider = true,
            isAnonymous = false
        )
    }
}
