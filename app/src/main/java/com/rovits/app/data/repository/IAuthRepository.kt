package com.rovits.app.data.repository

import com.rovits.app.data.model.AuthResult
import com.rovits.app.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface for authentication repository operations.
 *
 * This interface allows for easy testing and preview implementations
 * by providing a contract for authentication operations without
 * direct Firebase dependencies.
 */
interface IAuthRepository {

    /**
     * Gets the current authenticated user as a Flow.
     * Emits null if no user is authenticated.
     */
    fun getCurrentUserFlow(): Flow<User?>

    /**
     * Gets the current authenticated user synchronously.
     * Returns null if no user is authenticated.
     */
    fun getCurrentUser(): User?

    /**
     * Signs in a user with email and password.
     *
     * @param email User's email address
     * @param password User's password
     * @return AuthResult with User data on success or error on failure
     */
    suspend fun signInWithEmail(email: String, password: String): AuthResult<User>

    /**
     * Creates a new user account with email, password and full name.
     *
     * @param fullName User's full name
     * @param email User's email address
     * @param password User's password
     * @return AuthResult with User data on success or error on failure
     */
    suspend fun signUpWithEmail(fullName: String, email: String, password: String): AuthResult<User>

    /**
     * Signs in a user with Google authentication.
     *
     * @param idToken Google ID token from Google Sign-In
     * @return AuthResult with User data on success or error on failure
     */
    suspend fun signInWithGoogle(idToken: String): AuthResult<User>

    /**
     * Sends a password reset email to the specified address.
     *
     * @param email User's email address
     * @return AuthResult with Unit on success or error on failure
     */
    suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit>

    /**
     * Changes the current user's password.
     * First reauthenticates with the current password, then updates to new password.
     *
     * @param currentPassword User's current password for verification
     * @param newPassword New password to set
     * @return AuthResult with Unit on success or error on failure
     */
    suspend fun changePassword(currentPassword: String, newPassword: String): AuthResult<Unit>

    /**
     * Signs out the current user.
     */
    fun signOut()
}

