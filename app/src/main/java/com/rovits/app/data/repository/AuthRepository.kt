package com.rovits.app.data.repository

import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.data.remote.api.AuthApiService
import com.rovits.app.data.remote.dto.LoginRequest
import com.rovits.app.data.remote.dto.RegisterRequest
import com.rovits.app.data.remote.dto.SocialLoginRequest
import com.rovits.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody // YENİ IMPORT
import org.json.JSONObject // YENİ IMPORT
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton
import android.content.Context
import com.rovits.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import android.util.Log

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) {

    /**
     * Retrofit'in hata gövdesini (errorBody) JSON'a dönüştürür
     * ve içindeki "message" alanını alır.
     */
    private fun parseErrorMessage(errorBody: ResponseBody?): String {
        return try {
            val errorJsonString = errorBody?.string()
            if (errorJsonString.isNullOrEmpty()) {
                context.getString(R.string.error_unknown_response)
            } else {
                val jsonObject = JSONObject(errorJsonString)
                jsonObject.getString("message") ?: context.getString(R.string.error_cant_parse_error_message)
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error parsing error message", e)
            context.getString(R.string.error_parsing_response)
        }
    }

    fun login(email: String, password: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.login(LoginRequest(email, password))

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Token'ları kaydet
                preferencesManager.saveJwtToken(authResponse.token)
                authResponse.refreshToken?.let {
                    preferencesManager.saveRefreshToken(it)
                }
                preferencesManager.saveUserEmail(authResponse.user.email)

                emit(Resource.Success(authResponse.token))
            } else {
                val errorMessage = parseErrorMessage(response.errorBody())
                emit(Resource.Error(errorMessage))
            }
        } catch (e: SocketTimeoutException) {
            Log.e("AuthRepository", "Login timeout", e)
            emit(Resource.Error(context.getString(R.string.error_timeout)))
        } catch (e: IOException) {
            Log.e("AuthRepository", "Login network error", e)
            emit(Resource.Error(context.getString(R.string.error_network)))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login error", e)
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    fun register(name: String, email: String, password: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = authApi.register(RegisterRequest(name, email, password))

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Token'ları kaydet (kayıt sonrası otomatik giriş)
                preferencesManager.saveJwtToken(authResponse.token)
                authResponse.refreshToken?.let {
                    preferencesManager.saveRefreshToken(it)
                }
                preferencesManager.saveUserEmail(authResponse.user.email)

                emit(Resource.Success(authResponse.token))
            } else {
                val errorMessage = parseErrorMessage(response.errorBody())
                emit(Resource.Error(errorMessage))
            }
        } catch (e: SocketTimeoutException) {
            Log.e("AuthRepository", "Register timeout", e)
            emit(Resource.Error(context.getString(R.string.error_timeout)))
        } catch (e: IOException) {
            Log.e("AuthRepository", "Register network error", e)
            emit(Resource.Error(context.getString(R.string.error_network)))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Register error", e)
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }


    fun socialLogin(firebaseToken: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.socialLogin(SocialLoginRequest(firebaseToken, "google"))

            if (response.isSuccessful && response.body() != null) {
                val authResponse = response.body()!!

                // Token'ları kaydet
                preferencesManager.saveJwtToken(authResponse.token)
                authResponse.refreshToken?.let {
                    preferencesManager.saveRefreshToken(it)
                }
                preferencesManager.saveUserEmail(authResponse.user.email)

                emit(Resource.Success(authResponse.token))
            } else {
                val errorMessage = parseErrorMessage(response.errorBody())
                emit(Resource.Error(errorMessage))
            }
        } catch (e: SocketTimeoutException) {
            Log.e("AuthRepository", "Social login timeout", e)
            emit(Resource.Error(context.getString(R.string.error_timeout)))
        } catch (e: IOException) {
            Log.e("AuthRepository", "Social login network error", e)
            emit(Resource.Error(context.getString(R.string.error_network)))
        } catch (e: Exception) {
            Log.e("AuthRepository", "Social login error", e)
            emit(Resource.Error(e.localizedMessage ?: context.getString(R.string.error_unknown)))
        }
    }

    suspend fun logout() {
        preferencesManager.clearAll()
    }
}