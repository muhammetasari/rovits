package com.rovits.app.util

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {
    private const val PREFS_NAME = "locale_prefs"
    private const val KEY_LANGUAGE = "language_key"

    /**
     * Context'e dil ayarını uygular
     */
    fun setLocale(context: Context, languageCode: String? = null): Context {
        val language = if (languageCode != null) {
            saveLanguage(context, languageCode)
            Language.fromCode(languageCode)
        } else {
            val savedLanguage = getSavedLanguage(context)
            savedLanguage?.let { Language.fromCode(it) } ?: Language.getSystemLanguage()
        }

        return updateResources(context, language)
    }

    /**
     * Kaydedilmiş dili al
     */
    fun getSavedLanguage(context: Context): String? {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_LANGUAGE, null)
    }

    /**
     * Dil ayarını kaydet
     */
    fun saveLanguage(context: Context, languageCode: String) {
        val prefs = getPreferences(context)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    /**
     * Mevcut dili al
     */
    fun getCurrentLanguage(context: Context): Language {
        val savedLanguage = getSavedLanguage(context)
        return savedLanguage?.let { Language.fromCode(it) } ?: Language.getSystemLanguage()
    }

    /**
     * Resources'u güncelle
     */
    private fun updateResources(context: Context, language: Language): Context {
        val locale = language.locale
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(android.os.LocaleList(locale))
        }

        return context.createConfigurationContext(config)
    }

    /**
     * SharedPreferences instance'ı al
     */
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}

