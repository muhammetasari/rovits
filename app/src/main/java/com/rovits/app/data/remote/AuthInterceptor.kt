package com.rovits.app.data.remote

import com.rovits.app.BuildConfig
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.util.LocaleHelper
import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.UUID
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    private val apiKey: String = BuildConfig.API_KEY

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. API Key (Her zaman gerekli)
        if (apiKey.isNotEmpty()) {
            requestBuilder.addHeader(ApiConstants.HEADER_API_KEY, apiKey)
            Log.d(TAG, "API Key added")
        } else {
            Log.w(TAG, "API Key is empty!")
        }

        // 2. JWT Token (Login sonrası gerekli)
        val jwtToken = runBlocking {
            try {
                preferencesManager.getJwtToken().first()
            } catch (e: Exception) {
                Log.e(TAG, "Error getting JWT token", e)
                null
            }
        }

        jwtToken?.let {
            requestBuilder.addHeader(ApiConstants.HEADER_AUTHORIZATION, "Bearer $it")
            Log.d(TAG, "JWT Token added")
        }

        // 3. Accept-Language (i18n için)
        val currentLanguage = LocaleHelper.getCurrentLanguage(context)
        requestBuilder.addHeader("Accept-Language", currentLanguage.code)
        Log.d(TAG, "Accept-Language: ${currentLanguage.code}")

        // 4. X-Correlation-ID (Log tracking için)
        val correlationId = UUID.randomUUID().toString()
        requestBuilder.addHeader("X-Correlation-ID", correlationId)
        Log.d(TAG, "Correlation-ID: $correlationId")

        // 5. Content-Type (POST/PUT istekleri için)
        if (originalRequest.method in listOf("POST", "PUT", "PATCH")) {
            requestBuilder.addHeader(ApiConstants.HEADER_CONTENT_TYPE, "application/json")
        }

        val newRequest = requestBuilder.build()

        // Log request details
        Log.d(TAG, "Request: ${newRequest.method} ${newRequest.url}")

        return chain.proceed(newRequest)
    }
}