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

        // NOT: Backend Accept-Language header'ını desteklemiyor!
        // Backend her zaman Türkçe hata mesajları gönderiyor.
        // ErrorMessageMapper sınıfı backend'den gelen Türkçe mesajları
        // uygulama dilinde gösterilmek üzere çeviriyor.
        // TODO: Backend güncellendiğinde Accept-Language desteği eklenebilir.
    }
}
