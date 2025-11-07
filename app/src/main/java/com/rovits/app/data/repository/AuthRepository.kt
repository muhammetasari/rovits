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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val preferencesManager: PreferencesManager
) {

    /**
     * Retrofit'in hata gövdesini (errorBody) JSON'a dönüştürür
     * ve içindeki "message" alanını alır.
     */
    private fun parseErrorMessage(errorBody: ResponseBody?): String {
        return try {
            val errorJsonString = errorBody?.string()
            if (errorJsonString.isNullOrEmpty()) {
                "Bilinmeyen bir hata oluştu."
            } else {
                val jsonObject = JSONObject(errorJsonString)
                jsonObject.getString("message") ?: "Hata mesajı okunamadı."
            }
        } catch (e: Exception) {
            "Yanıt işlenirken hata oluştu."
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
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
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
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    fun socialLogin(firebaseToken: String, provider: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())

            val response = authApi.socialLogin(SocialLoginRequest(firebaseToken, provider))

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
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    suspend fun logout() {
        preferencesManager.clearAll()
    }
}