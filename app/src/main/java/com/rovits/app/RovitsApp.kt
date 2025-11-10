package com.rovits.app

import android.app.Application
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import javax.inject.Inject
import com.rovits.app.data.local.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@HiltAndroidApp
class RovitsApp : Application() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        initializeLocale()
    }

    private fun initializeLocale() {
        applicationScope.launch {
            try {
                val savedLanguage = preferencesManager.getLanguage().first()
                val locale = if (savedLanguage.isNullOrEmpty()) {
                    Locale.getDefault()
                } else {
                    Locale(savedLanguage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
