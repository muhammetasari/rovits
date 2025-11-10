package com.rovits.app.presentation.settings

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.util.Language
import com.rovits.app.util.LocaleManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    init {
        viewModelScope.launch {
            val savedLanguage = preferencesManager.getLanguage().first()
            _currentLanguage.value = if (savedLanguage != null) {
                Language.fromCode(savedLanguage)
            } else {
                Language.getSystemLanguage()
            }
        }
    }

    fun changeLanguage(language: Language, context: Context) {
        viewModelScope.launch {
            // Tercihi kaydet
            preferencesManager.setLanguage(language.code)
            _currentLanguage.value = language

            // Locale'i uygula ve activity'yi restart et
            LocaleManager.setLocale(context, language)
            (context as? Activity)?.let {
                LocaleManager.restartActivity(it)
            }
        }
    }
}