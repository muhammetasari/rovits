package com.rovits.app.util

import java.util.Locale

enum class Language(
    val code: String,
    val displayName: String,
    val locale: Locale
) {
    ENGLISH("en", "English", Locale.ENGLISH),
    TURKISH("tr", "Türkçe", Locale("tr", "TR")),
    GERMAN("de", "Deutsch", Locale.GERMAN);

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