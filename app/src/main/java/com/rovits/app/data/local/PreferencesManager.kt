package com.rovits.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "rovits_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore by lazy { context.dataStore }

    companion object {
        private val API_KEY = stringPreferencesKey("api_key")
        private val JWT_TOKEN = stringPreferencesKey("jwt_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val LANGUAGE = stringPreferencesKey("language") // YENİ
    }

    // API Key
    suspend fun saveApiKey(apiKey: String) {
        dataStore.edit { preferences ->
            preferences[API_KEY] = apiKey
        }
    }

    fun getApiKey(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[API_KEY]
        }
    }

    // JWT Token
    suspend fun saveJwtToken(token: String) {
        dataStore.edit { preferences ->
            preferences[JWT_TOKEN] = token
        }
    }

    fun getJwtToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[JWT_TOKEN]
        }
    }

    // Refresh Token
    suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = token
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN]
        }
    }

    // User Email
    suspend fun saveUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[USER_EMAIL] = email
        }
    }

    fun getUserEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }
    }

    // Language - YENİ
    suspend fun saveLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[LANGUAGE] = languageCode
        }
    }

    fun getLanguage(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[LANGUAGE]
        }
    }

    // Clear all data (logout)
    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}