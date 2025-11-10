package com.rovits.app.util

import android.util.Base64
import org.json.JSONObject
import java.util.Date

object JwtValidator {

    /**
     * JWT token'ın geçerli olup olmadığını kontrol eder
     * @param token JWT token string
     * @return Token geçerli mi?
     */
    fun isTokenValid(token: String?): Boolean {
        if (token.isNullOrEmpty()) return false

        return try {
            val parts = token.split(".")
            if (parts.size != 3) return false

            // Payload'u decode et
            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP))
            val json = JSONObject(payload)

            // Expiration time'ı kontrol et
            if (json.has("exp")) {
                val expirationTime = json.getLong("exp") * 1000 // Saniyeden milisaniyeye
                val currentTime = Date().time

                // Token expire olmamış mı?
                return currentTime < expirationTime
            }

            // Expiration yoksa token'ı geçerli say (güvenli değil ama fallback)
            true
        } catch (_: Exception) {
            // Parse hatası varsa token geçersiz
            false
        }
    }
}