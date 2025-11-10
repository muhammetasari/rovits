package com.rovits.app.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleManager {

    /**
     * Context'e locale uygular (Activity restart gerekir)
     */
    fun setLocale(context: Context, language: Language): Context {
        val locale = language.locale
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    /**
     * Activity'yi yeniden başlatır (dil değişimi için)
     */
    fun restartActivity(activity: Activity) {
        activity.finish()
        activity.startActivity(activity.intent)
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * Context'ten mevcut dili alır
     */
    fun getCurrentLanguage(context: Context): Language {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        return Language.fromCode(locale.language)
    }
}