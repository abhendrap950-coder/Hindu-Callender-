package com.example.ui.theme

import android.app.Activity
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

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = Color.Black,
    primaryContainer = MaroonDark,
    onPrimaryContainer = GoldLight,
    secondary = SaffronSecondary,
    onSecondary = Color.White,
    secondaryContainer = SacredSurfaceVariantDark,
    onSecondaryContainer = GoldLight,
    tertiary = GoldLight,
    background = SacredBgDark,
    onBackground = TextPrimaryDark,
    surface = SacredSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SacredSurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    error = InauspiciousRed,
    outline = Color(0xFF6B453E)
)

private val LightColorScheme = lightColorScheme(
    primary = MaroonPrimary,
    onPrimary = Color.White,
    primaryContainer = SacredSurfaceVariantLight,
    onPrimaryContainer = MaroonPrimary,
    secondary = SaffronSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = MaroonDark,
    tertiary = GoldDark,
    background = SacredBgLight,
    onBackground = TextPrimaryLight,
    surface = SacredSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SacredSurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    error = InauspiciousRed,
    outline = Color(0xFFE2C9B8)
)

@Composable
fun HinduPanchangTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
