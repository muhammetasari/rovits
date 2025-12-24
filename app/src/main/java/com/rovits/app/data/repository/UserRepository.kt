package com.rovits.app.data.repository

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.rovits.app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Gerçek kullanıcı repository'si. Firebase üzerinden kullanıcı verisi sağlar.
 */
class UserRepository : IUserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun getCurrentUser(): Flow<User?> = flow {
        val firebaseUser = auth.currentUser
        if (firebaseUser != null) {
            val isPasswordProvider = firebaseUser.providerData.any {
                it.providerId == EmailAuthProvider.PROVIDER_ID || it.providerId == "password"
            }
            emit(
                User(
                    uid = firebaseUser.uid,
                    fullName = firebaseUser.displayName ?: "",
                    email = firebaseUser.email ?: "",
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    isPasswordProvider = isPasswordProvider,
                    isAnonymous = firebaseUser.isAnonymous
                )
            )
        } else {
            emit(null)
        }
    }
}

