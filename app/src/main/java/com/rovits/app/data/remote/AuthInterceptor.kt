package com.rovits.app.data.remote

import com.rovits.app.BuildConfig
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.util.LocaleHelper
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) : Interceptor {

    private val apiKey: String = BuildConfig.API_KEY

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // JWT Token'ı DataStore'dan al
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

        // Accept-Language header ekle (uygulama dilini backend'e bildir)
        val currentLanguage = LocaleHelper.getCurrentLanguage(context)
        requestBuilder.addHeader("Accept-Language", currentLanguage.code)

        // Optional: X-Correlation-ID ekle (log tracking için)
        val correlationId = java.util.UUID.randomUUID().toString()
        requestBuilder.addHeader("X-Correlation-ID", correlationId)

        val newRequest = requestBuilder.build()
        return chain.proceed(newRequest)
    }
}