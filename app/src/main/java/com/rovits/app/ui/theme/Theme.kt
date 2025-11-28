package com.rovits.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


/**
 * Dark theme color scheme using Material Design 3 guidelines
 */
private val DarkColorScheme = darkColorScheme(
    primary = RovitsOrange,
    onPrimary = Color.White,
    primaryContainer = RovitsOrangeDark,
    onPrimaryContainer = Color.White,

    secondary = RovitsOrangeLight,
    onSecondary = Gray900,

    tertiary = WarningOrange,
    onTertiary = Color.White,

    background = DarkBackground,
    onBackground = Gray50,

    surface = DarkSurface,
    onSurface = Gray50,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Gray400,

    error = DarkError,
    onError = Color.White,

    outline = DarkOutline,
    outlineVariant = DarkSurfaceVariant
)

/**
 * Light theme color scheme using Material Design 3 guidelines
 */
private val LightColorScheme = lightColorScheme(
    primary = RovitsOrange,
    onPrimary = Color.White,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = Gray900,

    secondary = RovitsOrangeDark,
    onSecondary = Color.White,
    secondaryContainer = Gray50,
    onSecondaryContainer = Gray900,

    tertiary = SuccessGreen,
    onTertiary = Color.White,

    background = Color.White,
    onBackground = Gray900,

    surface = Color.White,
    onSurface = Gray900,
    surfaceVariant = Gray50,
    onSurfaceVariant = Gray600,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    outline = Gray200,
    outlineVariant = LightOutlineVariant
)

/**
 * Rovits App Theme - Material Design 3 standartlarına uygun özel tema sistemi
 *
 * Dinamik tema desteği kaldırılmıştır. Uygulama, tüm cihazlarda tutarlı bir görünüm için
 * özel olarak tanımlanmış renk paletini kullanır.
 *
 * @param darkTheme Dark theme kullanılıp kullanılmayacağı. Varsayılan olarak sistem ayarını takip eder.
 * @param content Temalı içerik composable'ı.
 */
@Composable
fun RovitsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}