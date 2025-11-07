package com.rovits.app.data.repository

import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.data.remote.api.AuthApiService
import com.rovits.app.data.remote.dto.LoginRequest
import com.rovits.app.data.remote.dto.SocialLoginRequest
import com.rovits.app.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApiService,
    private val preferencesManager: PreferencesManager
) {

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
                emit(Resource.Error(response.message() ?: "Login failed"))
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
                emit(Resource.Error(response.message() ?: "Social login failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    suspend fun logout() {
        preferencesManager.clearAll()
    }
}