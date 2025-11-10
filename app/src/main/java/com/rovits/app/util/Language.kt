package com.rovits.app.util

import androidx.annotation.StringRes
import com.rovits.app.R
import java.util.Locale

enum class Language(
    val code: String,
    val displayName: String,
    @StringRes val stringResId: Int,
    val locale: Locale
) {
    ENGLISH("en", "English", R.string.language_english, Locale.ENGLISH),
    TURKISH("tr", "Türkçe", R.string.language_turkish, Locale("tr", "TR")),
    GERMAN("de", "Deutsch", R.string.language_german, Locale.GERMAN);

    companion object {
        fun fromCode(code: String): Language {
            return entries.find { it.code == code } ?: ENGLISH
        }

        fun getSystemLanguage(): Language {
            val systemLang = Locale.getDefault().language
            return entries.find { it.code == systemLang } ?: ENGLISH
        }
    }
}