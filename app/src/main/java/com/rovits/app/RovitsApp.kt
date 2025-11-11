package com.rovits.app

import android.app.Application
import android.content.Context
import com.rovits.app.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RovitsApp : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base))
    }

    override fun onCreate() {
        super.onCreate()
        // Dil ayarını uygula
        LocaleHelper.setLocale(this)
    }
}
