package com.rovits.app.data.repository.fake

import com.rovits.app.data.model.AuthResult
import com.rovits.app.data.model.User
import com.rovits.app.data.repository.IAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Fake implementation of IAuthRepository for testing and previews.
 *
 * This implementation provides mock data and simulates authentication
 * operations without requiring actual Firebase connectivity.
 */
class FakeAuthRepository : IAuthRepository {

    private var currentUser: User? = MOCK_USER

    override fun getCurrentUserFlow(): Flow<User?> = flowOf(currentUser)

    override fun getCurrentUser(): User? = currentUser

    override suspend fun signInWithEmail(email: String, password: String): AuthResult<User> {
        return if (email.isNotEmpty() && password.isNotEmpty()) {
            currentUser = MOCK_USER.copy(email = email)
            AuthResult.Success(currentUser!!)
        } else {
            AuthResult.Error(com.rovits.app.util.error.AppException.AuthError("ERROR_INVALID_CREDENTIALS"))
        }
    }

    override suspend fun signUpWithEmail(
        fullName: String,
        email: String,
        password: String
    ): AuthResult<User> {
        return if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            currentUser = User(
                uid = "fake_uid_${System.currentTimeMillis()}",
                fullName = fullName,
                email = email,
                photoUrl = null
            )
            AuthResult.Success(currentUser!!)
        } else {
            AuthResult.Error(com.rovits.app.util.error.AppException.AuthError("ERROR_INVALID_INPUT"))
        }
    }

    override suspend fun signInWithGoogle(idToken: String): AuthResult<User> {
        return if (idToken.isNotEmpty()) {
            currentUser = MOCK_USER.copy(
                fullName = "Google User",
                photoUrl = "https://lh3.googleusercontent.com/a/default-user"
            )
            AuthResult.Success(currentUser!!)
        } else {
            AuthResult.Error(com.rovits.app.util.error.AppException.AuthError("ERROR_INVALID_TOKEN"))
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit> {
        return if (email.isNotEmpty()) {
            AuthResult.Success(Unit)
        } else {
            AuthResult.Error(com.rovits.app.util.error.AppException.AuthError("ERROR_INVALID_EMAIL"))
        }
    }

    override fun signOut() {
        currentUser = null
    }

    companion object {
        /**
         * Mock user for preview and testing purposes
         */
        val MOCK_USER = User(
            uid = "preview_user_123",
            fullName = "Önizleme Kullanıcı",
            email = "preview@rovits.com",
            photoUrl = null
        )
    }
}

