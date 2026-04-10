package com.survivalwiki.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private fun darkColorSchemeFor(accent: Color) = darkColorScheme(
    primary = accent,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = ErrorRed
)

private fun lightColorSchemeFor(accent: Color) = lightColorScheme(
    primary = accent,
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF121212),
    onSurface = Color(0xFF121212),
    onSurfaceVariant = Color(0xFF555555)
)

@Composable
fun SurvivalWikiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColorName: String = "orange",
    content: @Composable () -> Unit
) {
    val accentColor = when (accentColorName) {
        "olive" -> AccentOlive
        "blue" -> AccentBlue
        else -> AccentOrange
    }

    val colorScheme = if (darkTheme) darkColorSchemeFor(accentColor) else lightColorSchemeFor(accentColor)
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}