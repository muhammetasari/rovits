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
            // USER Hataları (USER_xxx)
            backendMessage.contains("USER_001", ignoreCase = true) ||
            backendMessage.contains("Kullanıcı bulunamadı", ignoreCase = true) ||
            backendMessage.contains("User not found", ignoreCase = true) ->
                context.getString(R.string.error_user_not_found)

            backendMessage.contains("USER_002", ignoreCase = true) ||
            backendMessage.contains("E-posta zaten kullanımda", ignoreCase = true) ||
            backendMessage.contains("Email already exists", ignoreCase = true) ||
            backendMessage.contains("Email already in use", ignoreCase = true) ->
                context.getString(R.string.error_email_already_exists)

            backendMessage.contains("USER_003", ignoreCase = true) ||
            backendMessage.contains("Geçersiz kimlik bilgileri", ignoreCase = true) ||
            backendMessage.contains("Invalid credentials", ignoreCase = true) ||
            backendMessage.contains("Geçersiz e-posta veya şifre", ignoreCase = true) ->
                context.getString(R.string.error_invalid_credentials)

            // AUTH Hataları (AUTH_xxx)
            backendMessage.contains("AUTH_001", ignoreCase = true) ||
            backendMessage.contains("Token süresi doldu", ignoreCase = true) ||
            backendMessage.contains("Token expired", ignoreCase = true) ||
            backendMessage.contains("jwt expired", ignoreCase = true) ->
                context.getString(R.string.error_token_expired)

            backendMessage.contains("AUTH_002", ignoreCase = true) ||
            backendMessage.contains("AUTH_011", ignoreCase = true) ||
            backendMessage.contains("Geçersiz token", ignoreCase = true) ||
            backendMessage.contains("Invalid token", ignoreCase = true) ->
                context.getString(R.string.error_invalid_token)

            backendMessage.contains("AUTH_003", ignoreCase = true) ||
            backendMessage.contains("AUTH_007", ignoreCase = true) ||
            backendMessage.contains("Yetkisiz erişim", ignoreCase = true) ||
            backendMessage.contains("Unauthorized", ignoreCase = true) ->
                context.getString(R.string.error_unauthorized)

            backendMessage.contains("AUTH_005", ignoreCase = true) ||
            backendMessage.contains("Firebase token geçersiz", ignoreCase = true) ||
            backendMessage.contains("Firebase token doğrulanamadı", ignoreCase = true) ||
            backendMessage.contains("Firebase token invalid", ignoreCase = true) ||
            backendMessage.contains("Firebase token verification failed", ignoreCase = true) ->
                context.getString(R.string.error_google_firebase_token)

            backendMessage.contains("AUTH_009", ignoreCase = true) ||
            backendMessage.contains("Email not verified", ignoreCase = true) ||
            backendMessage.contains("E-posta doğrulanmadı", ignoreCase = true) ||
            backendMessage.contains("verify your email", ignoreCase = true) ->
                context.getString(R.string.error_email_not_verified)

            // VALIDATION Hataları (VAL_xxx)
            backendMessage.contains("VAL_001", ignoreCase = true) ||
            backendMessage.contains("Malformed JSON request", ignoreCase = true) ||
            backendMessage.contains("JSON parse error", ignoreCase = true) ||
            backendMessage.contains("Hatalı JSON", ignoreCase = true) ->
                context.getString(R.string.error_bad_request)

            backendMessage.contains("VAL_002", ignoreCase = true) ||
            backendMessage.contains("Geçersiz e-posta", ignoreCase = true) ||
            backendMessage.contains("Invalid email", ignoreCase = true) ->
                context.getString(R.string.error_invalid_email_format)

            backendMessage.contains("VAL_003", ignoreCase = true) ||
            backendMessage.contains("VAL_010", ignoreCase = true) ||
            backendMessage.contains("Şifre en az", ignoreCase = true) ||
            backendMessage.contains("Password must be at least", ignoreCase = true) ->
                context.getString(R.string.error_password_too_short)

            backendMessage.contains("VAL_011", ignoreCase = true) ||
            backendMessage.contains("VAL_013", ignoreCase = true) ||
            backendMessage.contains("Şifre çok zayıf", ignoreCase = true) ||
            backendMessage.contains("Password too weak", ignoreCase = true) ->
                context.getString(R.string.error_password_too_weak)

            backendMessage.contains("VAL_004", ignoreCase = true) ||
            backendMessage.contains("Zorunlu alan", ignoreCase = true) ||
            backendMessage.contains("Required field", ignoreCase = true) ||
            backendMessage.contains("missing", ignoreCase = true) ||
            backendMessage.contains("NULL", ignoreCase = true) ->
                context.getString(R.string.error_required_fields)

            // EXTERNAL SERVICE Hataları (EXT_xxx)
            backendMessage.contains("EXT_001", ignoreCase = true) ||
            backendMessage.contains("EXT_002", ignoreCase = true) ||
            backendMessage.contains("Google API", ignoreCase = true) ->
                context.getString(R.string.error_google_sign_in_failed)

            backendMessage.contains("EXT_004", ignoreCase = true) ||
            backendMessage.contains("EXT_005", ignoreCase = true) ||
            backendMessage.contains("Firebase", ignoreCase = true) ->
                context.getString(R.string.error_google_firebase_token)

            backendMessage.contains("EXT_006", ignoreCase = true) ||
            backendMessage.contains("Zaman aşımı", ignoreCase = true) ||
            backendMessage.contains("Timeout", ignoreCase = true) ->
                context.getString(R.string.error_timeout)

            // DATABASE Hataları (DB_xxx)
            backendMessage.contains("DB_001", ignoreCase = true) ||
            backendMessage.contains("DB_002", ignoreCase = true) ||
            backendMessage.contains("Database", ignoreCase = true) ->
                context.getString(R.string.error_server_error)

            // HTTP Hataları
            backendMessage.contains("400", ignoreCase = true) ||
            backendMessage.contains("Bad Request", ignoreCase = true) ||
            backendMessage.contains("Hatalı istek", ignoreCase = true) ->
                context.getString(R.string.error_bad_request)

            backendMessage.contains("401", ignoreCase = true) ||
            backendMessage.contains("Unauthorized", ignoreCase = true) ||
            backendMessage.contains("Yetkisiz", ignoreCase = true) ->
                context.getString(R.string.error_unauthorized)

            backendMessage.contains("403", ignoreCase = true) ||
            backendMessage.contains("Forbidden", ignoreCase = true) ||
            backendMessage.contains("Yasak", ignoreCase = true) ->
                context.getString(R.string.error_forbidden)

            backendMessage.contains("404", ignoreCase = true) ||
            backendMessage.contains("Not Found", ignoreCase = true) ||
            backendMessage.contains("Bulunamadı", ignoreCase = true) ->
                context.getString(R.string.error_not_found)

            backendMessage.contains("429", ignoreCase = true) ||
            backendMessage.contains("Too Many Requests", ignoreCase = true) ||
            backendMessage.contains("Çok fazla istek", ignoreCase = true) ->
                context.getString(R.string.error_too_many_requests)

            backendMessage.contains("500", ignoreCase = true) ||
            backendMessage.contains("Internal Server Error", ignoreCase = true) ||
            backendMessage.contains("Sunucu hatası", ignoreCase = true) ||
            backendMessage.contains("Server error", ignoreCase = true) ->
                context.getString(R.string.error_server_error)

            backendMessage.contains("503", ignoreCase = true) ||
            backendMessage.contains("Service Unavailable", ignoreCase = true) ||
            backendMessage.contains("Servis kullanılamıyor", ignoreCase = true) ->
                context.getString(R.string.error_server_error)

            // Network Hataları
            backendMessage.contains("Network", ignoreCase = true) ||
            backendMessage.contains("Bağlantı", ignoreCase = true) ||
            backendMessage.contains("Connection", ignoreCase = true) ||
            backendMessage.contains("No internet", ignoreCase = true) ||
            backendMessage.contains("İnternet yok", ignoreCase = true) ->
                context.getString(R.string.error_network)


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
