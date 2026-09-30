package com.tribalscholar.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryNavy,
    onPrimary = TextOnDark,
    primaryContainer = PrimaryNavyLight,
    onPrimaryContainer = TextOnDark,
    secondary = AccentCyan,
    onSecondary = TextOnDark,
    secondaryContainer = SurfaceMuted,
    onSecondaryContainer = PrimaryNavy,
    tertiary = AccentTeal,
    onTertiary = TextOnDark,
    background = SurfaceLight,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceMuted,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyanLight,
    onPrimary = PrimaryNavyDark,
    primaryContainer = PrimaryNavyLight,
    onPrimaryContainer = TextOnDark,
    secondary = AccentCyan,
    onSecondary = PrimaryNavyDark,
    background = PrimaryNavyDark,
    onBackground = TextOnDark,
    surface = PrimaryNavy,
    onSurface = TextOnDark,
    surfaceVariant = PrimaryNavyLight,
    onSurfaceVariant = SurfaceBorder,
    outline = PrimaryNavyLight
)

@Composable
fun TribalScholarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = PrimaryNavy.toArgb()
                window.navigationBarColor = PrimaryNavy.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
