package com.rovits.app.data.repository

import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.data.remote.api.AuthApiService
import com.rovits.app.data.remote.dto.*
import com.rovits.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import java.io.IOException
import java.net.SocketTimeoutException
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
            if (response.isSuccessful) {
                val apiResponse = response.body()

                if (apiResponse == null) {
                    Log.e(TAG, "Response body is null")
                    Resource.Error(context.getString(R.string.error_unknown))
                } else if (apiResponse.success && apiResponse.data != null) {
                    Resource.Success(apiResponse.data)
                } else if (!apiResponse.success && apiResponse.error != null) {
                    val errorMessage = errorMessageMapper.mapErrorMessage(apiResponse.error.message)
                    Log.w(TAG, "API error: ${apiResponse.error.code} - $errorMessage")
                    Resource.Error(errorMessage)
                } else {
                    Log.e(TAG, "Invalid API response structure")
                    Resource.Error(context.getString(R.string.error_unknown))
                }
            } else {
                val errorMessage = parseErrorFromErrorBody(response.errorBody()?.string())
                Log.e(TAG, "HTTP error ${response.code()}: $errorMessage")
                Resource.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling API response", e)
            Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown))
        }
    }

    /**
     * Parse error from error body (for non-200 responses)
     */
    private fun parseErrorFromErrorBody(errorBody: String?): String {
        return try {
            if (errorBody.isNullOrEmpty()) {
                context.getString(R.string.error_unknown_response)
            } else {
                val gson = com.google.gson.Gson()
                val apiResponse = gson.fromJson(errorBody, ApiResponse::class.java)

                if (apiResponse?.error != null) {
                    errorMessageMapper.mapErrorMessage(apiResponse.error.message)
                } else {
                    context.getString(R.string.error_cant_parse_error_message)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing error body", e)
            context.getString(R.string.error_parsing_response)
        }
    }

    // ==================== LOGIN ====================
    fun login(email: String, password: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.login(LoginRequest(email, password))
            val result = handleApiResponse(response)

            when (result) {
                is Resource.Success -> {
                    val authResponse = result.data!!
                    saveAuthData(authResponse)
                    emit(Resource.Success(authResponse.token))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: context.getString(R.string.error_unknown)))
                }
                is Resource.Loading -> {}
            }
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "Login timeout", e)
            emit(Resource.Error(context.getString(R.string.error_timeout)))
        } catch (e: IOException) {
            Log.e(TAG, "Login network error", e)
            emit(Resource.Error(context.getString(R.string.error_network)))
        } catch (e: Exception) {
            Log.e(TAG, "Login error", e)
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    // ==================== REGISTER ====================
    fun register(name: String, email: String, password: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.register(RegisterRequest(name, email, password))
            val result = handleApiResponse(response)

            when (result) {
                is Resource.Success -> {
                    val authResponse = result.data!!
                    saveAuthData(authResponse)
                    emit(Resource.Success(authResponse.token))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: context.getString(R.string.error_unknown)))
                }
                is Resource.Loading -> {}
            }
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "Register timeout", e)
            emit(Resource.Error(context.getString(R.string.error_timeout)))
        } catch (e: IOException) {
            Log.e(TAG, "Register network error", e)
            emit(Resource.Error(context.getString(R.string.error_network)))
        } catch (e: Exception) {
            Log.e(TAG, "Register error", e)
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    // ==================== SOCIAL LOGIN ====================
    fun socialLogin(firebaseToken: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.socialLogin(SocialLoginRequest(firebaseToken, "google"))
            val result = handleApiResponse(response)

            when (result) {
                is Resource.Success -> {
                    val authResponse = result.data!!
                    saveAuthData(authResponse)
                    emit(Resource.Success(authResponse.token))
                }
                is Resource.Error -> {
                    emit(Resource.Error(result.message ?: context.getString(R.string.error_unknown)))
                }
                is Resource.Loading -> {}
            }
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "Social login timeout", e)
            emit(Resource.Error(context.getString(R.string.error_timeout)))
        } catch (e: IOException) {
            Log.e(TAG, "Social login network error", e)
            emit(Resource.Error(context.getString(R.string.error_network)))
        } catch (e: Exception) {
            Log.e(TAG, "Social login error", e)
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    // ==================== LOGOUT ====================
    suspend fun logout(): Flow<Resource<Unit>> = flow {
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