package com.rovits.app.presentation.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.local.PreferencesManager
import com.rovits.app.util.Language
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow<Language?>(null)
    val selectedLanguage: StateFlow<Language?> = _selectedLanguage

    init {
        viewModelScope.launch {
            val languageCode = preferencesManager.getLanguage().first()
            _selectedLanguage.value = if (languageCode != null) {
                Language.fromCode(languageCode)
            } else {
                Language.getSystemLanguage()
            }
        }
    }

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            preferencesManager.setLanguage(language.code)
            _selectedLanguage.value = language
        }
    }
}
