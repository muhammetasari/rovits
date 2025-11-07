package com.rovits.app.data.remote

import com.rovits.app.data.local.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // API Key ve JWT Token'ı DataStore'dan al
        val apiKey = runBlocking { preferencesManager.getApiKey().first() }
        val jwtToken = runBlocking { preferencesManager.getJwtToken().first() }

        // Request builder
        val requestBuilder = originalRequest.newBuilder()

        // API Key'i her zaman ekle
        apiKey?.let {
            requestBuilder.addHeader(ApiConstants.HEADER_API_KEY, it)
        }

        // JWT Token varsa Authorization header'ına ekle
        jwtToken?.let {
            requestBuilder.addHeader(ApiConstants.HEADER_AUTHORIZATION, "Bearer $it")
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}