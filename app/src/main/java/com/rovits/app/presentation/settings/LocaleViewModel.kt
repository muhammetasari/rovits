package com.rovits.app.presentation.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LocaleViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun setLocale(locale: Locale) {
        viewModelScope.launch {
            // Save the selected language
            preferencesManager.saveLanguage(locale.toLanguageTag())

            // Update the application locale
            val appLocale = LocaleListCompat.create(locale)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

    suspend fun getSavedLanguage(): String? {
        return preferencesManager.getLanguage().first()
    }
}
