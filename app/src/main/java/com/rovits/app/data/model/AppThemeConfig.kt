package com.rovits.app.data.model

/**
 * Enum class representing the app's theme configuration options.
 *
 * Provides three theme modes:
 * - FOLLOW_SYSTEM: Follows the device's system theme (Light/Dark)
 * - LIGHT: Forces the app to use Light theme
 * - DARK: Forces the app to use Dark theme
 */
enum class AppThemeConfig {
    /** Follows the device's system theme setting */
    FOLLOW_SYSTEM,

    /** Forces Light theme regardless of system settings */
    LIGHT,

    /** Forces Dark theme regardless of system settings */
    DARK;

    /**
     * Returns a user-friendly display name for the theme option
     */
    fun getDisplayName(): String = when (this) {
        FOLLOW_SYSTEM -> "System Default"
        LIGHT -> "Light"
        DARK -> "Dark"
    }

    companion object {
        /**
         * Converts a string to AppThemeConfig enum, with FOLLOW_SYSTEM as default
         */
        fun fromString(value: String?): AppThemeConfig {
            return try {
                valueOf(value ?: FOLLOW_SYSTEM.name)
            } catch (_: IllegalArgumentException) {
                FOLLOW_SYSTEM
            }
        }
    }
}

