package com.runanywhere.startup_hackathon20.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7C4DFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF5E35B1),
    onPrimaryContainer = Color(0xFFEDE7F6),
    secondary = Color(0xFF00BFA5),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF00897B),
    onSecondaryContainer = Color(0xFFB2DFDB),
    tertiary = Color(0xFFFF6F00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE65100),
    onTertiaryContainer = Color(0xFFFFE0B2),
    error = Color(0xFFCF6679),
    onError = Color(0xFF000000),
    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0F0F0F),
    onBackground = Color(0xFFF0F0F0),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFFF0F0F0),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFD0D0D0),
    outline = Color(0xFF7C4DFF).copy(alpha = 0.4f)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6A1B9A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF4A148C),
    secondary = Color(0xFF00BFA5),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = Color(0xFF004D40),
    tertiary = Color(0xFFFF6F00),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE0B2),
    onTertiaryContainer = Color(0xFFE65100),
    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF424242),
    outline = Color(0xFF9E9E9E)
)

@Composable
fun Startup_hackathon20Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}