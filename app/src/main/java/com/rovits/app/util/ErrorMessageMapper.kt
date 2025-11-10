package com.rovits.app.util

import android.content.Context
import android.util.Log
import com.rovits.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Backend'den gelen hata mesajlarını (Türkçe) uygulamanın mevcut diline çevirir.
 *
 * ⚠️ ÖNEMLİ NOT: Backend Accept-Language header'ını desteklemiyor!
 * Backend her zaman Türkçe hata mesajları gönderiyor.
 * Bu sınıf client-side'da Türkçe -> Uygulama Dili (en, tr, de) çevirisi yapıyor.
 *
 * TODO: Backend güncellenip Accept-Language desteği eklendiğinde:
 * 1. AuthInterceptor'a Accept-Language header'ı eklenebilir
 * 2. Bu mapper sınıfı basitleştirilebilir veya fallback olarak kullanılabilir
 *
 * Tarih: 10 Kasım 2025
 */
@Singleton
class ErrorMessageMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val TAG = "ErrorMessageMapper"
    }

    /**
     * Backend'den gelen hata mesajını map eder ve uygulamanın mevcut diline çevirir.
     * Eğer mesaj bilinmiyorsa, orijinal mesajı döner.
     */
    fun mapErrorMessage(backendMessage: String): String {
        Log.d(TAG, "Mapping backend message: $backendMessage")

        val mappedMessage = when {
            // Login/Auth hataları
            // Backend'den gelen Türkçe mesaj: "Geçersiz e-posta veya şifre"
            backendMessage.contains("Geçersiz e-posta veya şifre", ignoreCase = true) ||
            backendMessage.contains("Invalid email or password", ignoreCase = true) ||
            backendMessage.contains("Invalid credentials", ignoreCase = true) ->
                context.getString(R.string.error_invalid_credentials)

            backendMessage.contains("Kullanıcı bulunamadı", ignoreCase = true) ||
            backendMessage.contains("User not found", ignoreCase = true) ->
                context.getString(R.string.error_user_not_found)

            backendMessage.contains("E-posta zaten kullanımda", ignoreCase = true) ||
            backendMessage.contains("Email already exists", ignoreCase = true) ||
            backendMessage.contains("Email already in use", ignoreCase = true) ->
                context.getString(R.string.error_email_already_exists)

            backendMessage.contains("Geçersiz e-posta", ignoreCase = true) ||
            backendMessage.contains("Invalid email", ignoreCase = true) ->
                context.getString(R.string.error_invalid_email_format)

            backendMessage.contains("Şifre çok kısa", ignoreCase = true) ||
            backendMessage.contains("Password too short", ignoreCase = true) ||
            backendMessage.contains("Password must be at least", ignoreCase = true) ->
                context.getString(R.string.error_password_too_short)

            backendMessage.contains("Şifre çok zayıf", ignoreCase = true) ||
            backendMessage.contains("Password too weak", ignoreCase = true) ->
                context.getString(R.string.error_password_too_weak)

            // Token hataları
            backendMessage.contains("Token süresi doldu", ignoreCase = true) ||
            backendMessage.contains("Token expired", ignoreCase = true) ||
            backendMessage.contains("jwt expired", ignoreCase = true) ->
                context.getString(R.string.error_token_expired)

            backendMessage.contains("Geçersiz token", ignoreCase = true) ||
            backendMessage.contains("Invalid token", ignoreCase = true) ->
                context.getString(R.string.error_invalid_token)

            backendMessage.contains("Yetki yok", ignoreCase = true) ||
            backendMessage.contains("Unauthorized", ignoreCase = true) ||
            backendMessage.contains("Not authorized", ignoreCase = true) ->
                context.getString(R.string.error_unauthorized)

            // Genel hatalar
            backendMessage.contains("Sunucu hatası", ignoreCase = true) ||
            backendMessage.contains("Server error", ignoreCase = true) ||
            backendMessage.contains("Internal server error", ignoreCase = true) ->
                context.getString(R.string.error_server_error)

            backendMessage.contains("Bağlantı hatası", ignoreCase = true) ||
            backendMessage.contains("Connection error", ignoreCase = true) ->
                context.getString(R.string.error_network)

            backendMessage.contains("Zaman aşımı", ignoreCase = true) ||
            backendMessage.contains("Timeout", ignoreCase = true) ->
                context.getString(R.string.error_timeout)

            // Validation hataları
            backendMessage.contains("Eksik alan", ignoreCase = true) ||
            backendMessage.contains("Missing field", ignoreCase = true) ||
            backendMessage.contains("Required field", ignoreCase = true) ->
                context.getString(R.string.error_required_fields)

            backendMessage.contains("Geçersiz veri", ignoreCase = true) ||
            backendMessage.contains("Invalid data", ignoreCase = true) ->
                context.getString(R.string.error_invalid_data)

            // Eğer hiçbir pattern match etmezse, orijinal mesajı döner
            else -> {
                Log.w(TAG, "No mapping found for message: $backendMessage")
                backendMessage
            }
        }

        Log.d(TAG, "Mapped message: $mappedMessage")
        return mappedMessage
    }
}
