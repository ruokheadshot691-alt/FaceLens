package com.tracelens.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TraceAccent,
    onPrimary = TraceInk,
    primaryContainer = TraceAccentDark,
    onPrimaryContainer = TraceText,
    secondary = ColorTokens.darkSecondary,
    onSecondary = TraceText,
    secondaryContainer = TracePanel,
    onSecondaryContainer = TraceText,
    background = TraceInk,
    onBackground = TraceText,
    surface = TraceInk,
    onSurface = TraceText,
    surfaceVariant = TraceSlate,
    onSurfaceVariant = TraceMuted,
    outline = TraceLine,
    error = ColorTokens.darkError,
    errorContainer = ColorTokens.darkErrorContainer,
)

private val LightColorScheme = lightColorScheme(
    primary = ColorTokens.lightPrimary,
    onPrimary = TraceLightSurface,
    primaryContainer = Color(0xFFDDF4EE),
    onPrimaryContainer = Color(0xFF0C332D),
    secondary = ColorTokens.lightSecondary,
    onSecondary = TraceLightSurface,
    secondaryContainer = Color(0xFFE8EEEF),
    onSecondaryContainer = TraceLightText,
    background = TraceLightBackground,
    onBackground = TraceLightText,
    surface = TraceLightSurface,
    onSurface = TraceLightText,
    surfaceVariant = Color(0xFFEDF1F2),
    onSurfaceVariant = TraceLightMuted,
    outline = TraceLightLine,
    error = Color(0xFFB3261E),
    errorContainer = Color(0xFFF9DEDC),
)

private object ColorTokens {
    val darkSecondary = Color(0xFFB8C8C5)
    val darkError = Color(0xFFFFB4AB)
    val darkErrorContainer = Color(0xFF5C1714)
    val lightPrimary = Color(0xFF176B5B)
    val lightSecondary = Color(0xFF4E625E)
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val scheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = scheme,
        typography = Typography,
        content = content,
    )
}
