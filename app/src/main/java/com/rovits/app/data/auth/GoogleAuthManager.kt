package com.rovits.app.data.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rovits.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {
    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(): GetCredentialRequest {
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            context.getString(R.string.default_web_client_id)
        )
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
    }

    suspend fun getCredentialManager(): CredentialManager {
        return credentialManager
    }

    suspend fun signInWithCredential(credential: androidx.credentials.Credential): String? {
        return try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken
            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            val user = firebaseAuth.signInWithCredential(googleCredentials).await()
            user.user?.getIdToken(false)?.await()?.token
        } catch (e: GoogleIdTokenParsingException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun signOut() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}