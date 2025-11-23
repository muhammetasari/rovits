package com.rovits.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.rovits.app.data.model.AuthResult
import com.rovits.app.data.model.User
import com.rovits.app.util.error.AppException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.IOException

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Get current user as Flow
    fun getCurrentUserFlow(): Flow<User?> = flow {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            emit(
                User(
                    uid = firebaseUser.uid,
                    fullName = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString()
                )
            )
        } else {
            emit(null)
        }
    }

    // Get current user (synchronous)
    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser
        return if (firebaseUser != null) {
            User(
                uid = firebaseUser.uid,
                fullName = firebaseUser.displayName ?: "",
                email = firebaseUser.email ?: "",
                photoUrl = firebaseUser.photoUrl?.toString()
            )
        } else {
            null
        }
    }

    // Sign in with email and password
    suspend fun signInWithEmail(email: String, password: String): AuthResult<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                AuthResult.Success(
                    User(
                        uid = firebaseUser.uid,
                        fullName = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        photoUrl = firebaseUser.photoUrl?.toString()
                    )
                )
            } else {
                AuthResult.Error(AppException.AuthError("ERROR_USER_NOT_FOUND"))
            }
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(AppException.AuthError(e.errorCode, e.message))
        } catch (e: IOException) {
            AuthResult.Error(AppException.NetworkError(e.message))
        } catch (e: Exception) {
            AuthResult.Error(AppException.UnknownError(e.message))
        }
    }

    // Sign up with email, password and full name
    suspend fun signUpWithEmail(fullName: String, email: String, password: String): AuthResult<User> {
        return try {
            // Create user account
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                // Update profile with display name
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName)
                    .build()

                firebaseUser.updateProfile(profileUpdates).await()

                AuthResult.Success(
                    User(
                        uid = firebaseUser.uid,
                        fullName = fullName,
                        email = firebaseUser.email ?: "",
                        photoUrl = firebaseUser.photoUrl?.toString()
                    )
                )
            } else {
                AuthResult.Error(AppException.AuthError("ERROR_USER_NOT_FOUND"))
            }
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(AppException.AuthError(e.errorCode, e.message))
        } catch (e: IOException) {
            AuthResult.Error(AppException.NetworkError(e.message))
        } catch (e: Exception) {
            AuthResult.Error(AppException.UnknownError(e.message))
        }
    }

    // Sign in with Google
    suspend fun signInWithGoogle(idToken: String): AuthResult<User> {
        return try {
            android.util.Log.d("AuthRepository", "signInWithGoogle: Creating credential with idToken")
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            android.util.Log.d("AuthRepository", "signInWithGoogle: Signing in with Firebase")
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                android.util.Log.d("AuthRepository", "signInWithGoogle: Success - User: ${firebaseUser.email}")
                AuthResult.Success(
                    User(
                        uid = firebaseUser.uid,
                        fullName = firebaseUser.displayName ?: "",
                        email = firebaseUser.email ?: "",
                        photoUrl = firebaseUser.photoUrl?.toString()
                    )
                )
            } else {
                android.util.Log.e("AuthRepository", "signInWithGoogle: Firebase user is null")
                AuthResult.Error(AppException.AuthError("ERROR_USER_NOT_FOUND"))
            }
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "signInWithGoogle: Exception - ${e.message}", e)
            AuthResult.Error(AppException.AuthError(e.localizedMessage ?: "ERROR_UNKNOWN"))
        }
    }

    // Send password reset email
    suspend fun sendPasswordResetEmail(email: String): AuthResult<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthResult.Success(Unit)
        } catch (e: FirebaseAuthException) {
            AuthResult.Error(AppException.AuthError(e.errorCode, e.message))
        } catch (e: IOException) {
            AuthResult.Error(AppException.NetworkError(e.message))
        } catch (e: Exception) {
            AuthResult.Error(AppException.UnknownError(e.message))
        }
    }

    // Sign out
    fun signOut() {
        auth.signOut()
    }
}
