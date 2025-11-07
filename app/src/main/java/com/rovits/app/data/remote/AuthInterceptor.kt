package com.rovits.app.data.remote

import com.rovits.app.BuildConfig
import com.rovits.app.data.local.PreferencesManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager
) : Interceptor {

    private val apiKey: String = BuildConfig.API_KEY

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Sadece JWT Token'ı DataStore'dan al
        val jwtToken = runBlocking { preferencesManager.getJwtToken().first() }

        val requestBuilder = originalRequest.newBuilder()

        // API Key'i (BuildConfig'ten gelen) her zaman ekle
        if (apiKey.isNotEmpty()) {
            requestBuilder.addHeader(ApiConstants.HEADER_API_KEY, apiKey)
        }

        // JWT Token (DataStore'dan gelen) varsa Authorization header'ına ekle
        jwtToken?.let {
            requestBuilder.addHeader(ApiConstants.HEADER_AUTHORIZATION, "Bearer $it")
        }

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}