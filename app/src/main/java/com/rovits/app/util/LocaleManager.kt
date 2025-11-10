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

        return context.createConfigurationContext(config)
    }

    /**
     * Activity'yi yeniden başlatır (dil değişimi için)
     */
    fun restartActivity(activity: Activity) {
        activity.finish()
        activity.startActivity(activity.intent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            activity.overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_OPEN,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            @Suppress("DEPRECATION")
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    /**
     * Context'ten mevcut dili alır
     */
    fun getCurrentLanguage(context: Context): Language {
        val locale = context.resources.configuration.locales[0]
        return Language.fromCode(locale.language)
    }
}