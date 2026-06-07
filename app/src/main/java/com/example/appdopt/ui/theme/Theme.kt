package com.example.appdopt.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.appdopt.model.AppSettings
import com.example.appdopt.model.ColorBlindMode

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = BlueLight,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = BlueLight,
    background = BackgroundLight,
    surface = SurfaceWhite,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    surfaceVariant = CardBackground
)

@Composable
fun AppdoptTheme(
    settings: AppSettings = AppSettings(),
    content: @Composable () -> Unit
) {
    val primaryColor = when (settings.colorBlindMode) {
        ColorBlindMode.PROTANOPIA -> ProtanopiaPrimary
        ColorBlindMode.DEUTERANOPIA -> DeuteranopiaPrimary
        ColorBlindMode.TRITANOPIA -> TritanopiaPrimary
        ColorBlindMode.NONE -> if (settings.isHighContrast) Color.Black else BluePrimary
    }

    val colorScheme = if (settings.isDarkMode) {
        DarkColorScheme.copy(primary = primaryColor)
    } else {
        LightColorScheme.copy(primary = primaryColor)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
