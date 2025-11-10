package com.rovits.app.presentation.settings

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rovits.app.data.local.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LocaleViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val application: Application
) : ViewModel() {

    private val _currentLocale = MutableStateFlow(Locale.getDefault())
    val currentLocale = _currentLocale.asStateFlow()

    private val _recreateActivity = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val recreateActivity = _recreateActivity.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            preferencesManager.getLanguage()
                .distinctUntilChanged()
                .collect { lang ->
                    val locale = if (lang.isNullOrEmpty()) {
                        Locale.getDefault()
                    } else {
                        Locale(lang)
                    }
                    if (_currentLocale.value.language != locale.language) {
                        _currentLocale.value = locale
                        withContext(Dispatchers.Main) {
                            updateConfiguration(locale)
                        }
                    }
                }
        }
    }

    private fun updateConfiguration(locale: Locale) {
        try {
            val config = Configuration(application.resources.configuration)
            config.setLocale(locale)
            @Suppress("DEPRECATION")
            application.resources.updateConfiguration(config, application.resources.displayMetrics)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setLocale(locale: Locale) {
        viewModelScope.launch(Dispatchers.Default) {
            if (_currentLocale.value.language != locale.language) {
                preferencesManager.saveLanguage(locale.language)
                _currentLocale.value = locale
                withContext(Dispatchers.Main) {
                    updateConfiguration(locale)
                    _recreateActivity.tryEmit(Unit)
                }
            }
        }
    }

    companion object {
        suspend fun getSavedLocale(context: Context): Locale = withContext(Dispatchers.Default) {
            try {
                val preferencesManager = PreferencesManager(context)
                val language = preferencesManager.getLanguage().first()
                if (language.isNullOrEmpty()) Locale.getDefault() else Locale(language)
            } catch (e: Exception) {
                Locale.getDefault()
            }
        }
    }
}
