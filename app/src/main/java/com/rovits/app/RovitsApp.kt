package com.rovits.app

import android.app.Application
import android.content.Context
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.util.Language
import com.rovits.app.util.LocaleManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class RovitsApp : Application() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun attachBaseContext(base: Context) {
        val context = runBlocking {
            val languageCode = try {
                preferencesManager.getLanguage().first()
            } catch (_: Exception) {
                null
            }

            val language = if (languageCode != null) {
                Language.fromCode(languageCode)
            } else {
                Language.getSystemLanguage()
            }

            LocaleManager.setLocale(base, language)
        }
        super.attachBaseContext(context)
    }
}
