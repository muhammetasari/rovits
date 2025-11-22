package com.rovits.app.data.repository

import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.data.remote.api.AuthApiService
import com.rovits.app.data.remote.dto.*
import com.rovits.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import com.rovits.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import android.util.Log
import retrofit2.Response

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context,
    private val errorMessageMapper: com.rovits.app.util.ErrorMessageMapper
) {

    companion object {
        private const val TAG = "AuthRepository"
    }

    /**
     * Generic API response handler for new backend format
     */
    private fun <T> handleApiResponse(response: Response<ApiResponse<T>>): Resource<T> {
        return try {
            Log.d(TAG, "Response code: ${response.code()}, isSuccessful: ${response.isSuccessful}")
            if (response.isSuccessful) {
                val apiResponse = response.body()
                Log.d(TAG, "Response body success: ${apiResponse?.success}, error code: ${apiResponse?.error?.code}")

                if (apiResponse == null) {
                    Log.e(TAG, "Response body is null")
                    Resource.Error(context.getString(R.string.error_unknown))
                } else if (apiResponse.success && apiResponse.data != null) {
                    Resource.Success(apiResponse.data)
                } else if (!apiResponse.success && apiResponse.error != null) {
                    // Error code ve message'ı birleştirerek map et
                    val errorCode = apiResponse.error.code
                    val errorMessage = apiResponse.error.message
                    val combinedError = "$errorCode: $errorMessage"
                    val mappedMessage = errorMessageMapper.mapErrorMessage(combinedError)
                    Log.w(TAG, "API error: $errorCode - $mappedMessage")
                    Resource.Error(mappedMessage, errorCode)
                } else {
                    Log.e(TAG, "Invalid API response structure")
                    Resource.Error(context.getString(R.string.error_unknown))
                }
            } else {
                val (errorMessage, errorCode) = parseErrorFromErrorBodyWithCode(response.errorBody()?.string())
                Log.e(TAG, "HTTP error ${response.code()}: $errorMessage ($errorCode)")
                Resource.Error(errorMessage, errorCode)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling API response", e)
            Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown))
        }
    }

    /**
     * Parse error from error body (for non-200 responses), hem mesaj hem kod döndürür
     */
    private fun parseErrorFromErrorBodyWithCode(errorBody: String?): Pair<String, String?> {
        return try {
            if (errorBody.isNullOrEmpty()) {
                Pair(context.getString(R.string.error_unknown_response), null)
            } else {
                val gson = com.google.gson.Gson()
                try {
                    // Önce doğrudan ErrorDetail parse etmeyi dene
                    val rootObj = gson.fromJson(errorBody, com.google.gson.JsonObject::class.java)
                    val errorObj = rootObj?.getAsJsonObject("error")
                    val code = errorObj?.get("code")?.asString
                    val message = errorObj?.get("message")?.asString
                    if (code != null && message != null) {
                        val combined = "$code: $message"
                        Pair(errorMessageMapper.mapErrorMessage(combined), code)
                    } else {
                        // Fallback: eski generic parse
                        val apiResponse = gson.fromJson(errorBody, ApiResponse::class.java)
                        if (apiResponse?.error != null) {
                            Pair(errorMessageMapper.mapErrorMessage(apiResponse.error.message), apiResponse.error.code)
                        } else {
                            Pair(context.getString(R.string.error_cant_parse_error_message), null)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing errorBody as ErrorDetail: $errorBody", e)
                    Pair(context.getString(R.string.error_parsing_response), null)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing error body", e)
            Pair(context.getString(R.string.error_parsing_response), null)
        }
    }

    // ==================== LOGIN ====================
    fun login(firebaseToken: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = authApi.login(FirebaseTokenRequest(firebaseToken))
            val result = handleApiResponse(response)
            when (result) {
                is Resource.Success -> {
                    val authResponse = result.data
                    if (authResponse != null) {
                        preferencesManager.saveJwtToken(authResponse.token)
                        preferencesManager.saveRefreshToken(authResponse.refreshToken ?: "")
                        preferencesManager.saveUserEmail(authResponse.user.email)
                        emit(Resource.Success(authResponse.token))
                    } else {
                        emit(Resource.Error(context.getString(R.string.error_unknown)))
                    }
                }
                is Resource.Error -> emit(Resource.Error(
                    result.message ?: context.getString(R.string.error_unknown),
                    result.errorCode
                ))
                else -> {}
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    // ==================== REGISTER ====================
    fun register(firebaseToken: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = authApi.register(FirebaseTokenRequest(firebaseToken))
            val result = handleApiResponse(response)
            when (result) {
                is Resource.Success -> {
                    val authResponse = result.data
                    if (authResponse != null) {
                        preferencesManager.saveJwtToken(authResponse.token)
                        preferencesManager.saveRefreshToken(authResponse.refreshToken ?: "")
                        preferencesManager.saveUserEmail(authResponse.user.email)
                        emit(Resource.Success(authResponse.token))
                    } else {
                        emit(Resource.Error(context.getString(R.string.error_unknown)))
                    }
                }
                is Resource.Error -> emit(Resource.Error(
                    result.message ?: context.getString(R.string.error_unknown),
                    result.errorCode
                ))
                else -> {}
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    // ==================== LOGOUT ====================
    fun logout(): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())

            // Backend'e logout isteği gönder
            val response = authApi.logout()

            // Response'u kontrol et (başarısız olsa bile local data temizlenecek)
            if (response.isSuccessful) {
                Log.i(TAG, "Logout successful on backend")
            } else {
                Log.w(TAG, "Backend logout failed: ${response.code()}")
            }

            // Her durumda local data'yı temizle
            clearAuthData()
            emit(Resource.Success(Unit))

        } catch (e: Exception) {
            Log.e(TAG, "Logout error", e)
            // Hata olsa bile local data'yı temizle
            clearAuthData()
            emit(Resource.Success(Unit))
        }
    }

    // ==================== SEND EMAIL VERIFICATION ====================
    fun sendEmailVerification(firebaseToken: String): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            val response = authApi.sendEmailVerification(FirebaseTokenRequest(firebaseToken))
            val result = handleApiResponse(response)
            when (result) {
                is Resource.Success -> {
                    Log.i(TAG, "Email verification sent successfully")
                    emit(Resource.Success(Unit))
                }
                is Resource.Error -> {
                    Log.e(TAG, "Email verification failed: ${result.message}")
                    emit(Resource.Error(
                        result.message ?: context.getString(R.string.error_unknown),
                        result.errorCode
                    ))
                }
                else -> {}
            }
        } catch (e: Exception) {
            Log.e(TAG, "Email verification error", e)
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    // ==================== HELPER METHODS ====================
    private suspend fun saveAuthData(authResponse: AuthResponse) {
        preferencesManager.saveJwtToken(authResponse.token)
        authResponse.refreshToken?.let {
            preferencesManager.saveRefreshToken(it)
        }
        preferencesManager.saveUserEmail(authResponse.user.email)
        Log.i(TAG, "Auth data saved: ${authResponse.user.email}")
    }

    private suspend fun clearAuthData() {
        preferencesManager.clearAll()
        Log.i(TAG, "Auth data cleared")
    }
}