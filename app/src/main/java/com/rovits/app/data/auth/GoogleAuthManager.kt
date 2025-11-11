package com.rovits.app.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rovits.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth
) {
    private val credentialManager = CredentialManager.create(context)

    companion object {
        private const val TAG = "GoogleAuthManager"
    }

    fun signIn(): GetCredentialRequest {
        val webClientId = context.getString(R.string.default_web_client_id)
        Log.d(TAG, "Creating Google Sign-In request with web client ID: ${webClientId.take(20)}...")

        // Nonce oluştur (güvenlik için)
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // Tüm Google hesaplarını göster
            .setServerClientId(webClientId)
            .setNonce(hashedNonce)
            .setAutoSelectEnabled(false) // Kullanıcının manuel seçim yapmasına izin ver
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    fun getCredentialManager(): CredentialManager {
        return credentialManager
    }

    suspend fun signInWithCredential(credential: androidx.credentials.Credential): String? {
        return try {
            Log.d(TAG, "Processing Google credential...")
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val googleIdToken = googleIdTokenCredential.idToken
            Log.d(TAG, "Google ID token obtained, signing in with Firebase...")

            val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
            val user = firebaseAuth.signInWithCredential(googleCredentials).await()

            Log.d(TAG, "Firebase sign-in successful, getting ID token...")
            val token = user.user?.getIdToken(false)?.await()?.token

            if (token != null) {
                Log.d(TAG, "Firebase ID token obtained successfully")
            } else {
                Log.e(TAG, "Firebase ID token is null")
            }

            token
        } catch (e: GoogleIdTokenParsingException) {
            Log.e(TAG, "Google ID token parsing error", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error during Google sign-in", e)
            null
        }
    }

    fun signOut() {
        try {
            Log.d(TAG, "Signing out from Firebase...")
            firebaseAuth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Error during sign out", e)
        }
    }
}