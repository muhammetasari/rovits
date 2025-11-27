package com.rovits.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Sadece ana marka renklerini tanımlıyoruz - Geri kalan her şey Material Design 3 standartlarından gelecek
private val RovitsOrange = Color(0xFF83331D)
private val RovitsOrangeDark = Color(0xFFE56B47)
private val RovitsOrangeLight = Color(0xFFFF9A7F)

/**
 * Dark theme color scheme using Material Design 3 guidelines
 */
private val DarkColorScheme = darkColorScheme(
    primary = RovitsOrange,
    onPrimary = Color.White,
    primaryContainer = RovitsOrangeDark,
    onPrimaryContainer = Color.White,

    secondary = RovitsOrangeLight,
    onSecondary = Color(0xFF1A1A1A),

    tertiary = Color(0xFFF57C00),  // Warning orange
    onTertiary = Color.White,

    background = Color(0xFF121212),
    onBackground = Color(0xFFF5F5F5),

    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFF5F5F5),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFB3B3B3),

    error = Color(0xFFCF6679),
    onError = Color.White,

    outline = Color(0xFF3E3E3E),
    outlineVariant = Color(0xFF2C2C2C)
)

/**
 * Light theme color scheme using Material Design 3 guidelines
 */
private val LightColorScheme = lightColorScheme(
    primary = RovitsOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE5DC),
    onPrimaryContainer = Color(0xFF1A1A1A),

    secondary = RovitsOrangeDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF5F5F5),
    onSecondaryContainer = Color(0xFF1A1A1A),

    tertiary = Color(0xFF388E3C),  // Success green
    onTertiary = Color.White,

    background = Color.White,
    onBackground = Color(0xFF1A1A1A),

    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF666666),

    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFFB71C1C),

    outline = Color(0xFFE0E0E0),
    outlineVariant = Color(0xFFCCCCCC)
)

/**
 * Rovits App Theme - Uses Material Design 3 with dynamic color support
 *
 * Dynamic colors are enabled by default on Android 12+ devices, allowing the app
 * to adapt to the user's system theme automatically.
 *
 * @param darkTheme Whether to use dark theme. Defaults to system setting.
 * @param dynamicColor Whether to use dynamic colors from Android 12+. Defaults to true.
 * @param content The composable content to theme.
 */
@Composable
fun RovitsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}