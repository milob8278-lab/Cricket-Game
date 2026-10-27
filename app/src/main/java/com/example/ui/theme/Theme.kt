package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = CricketGreenLight,
    onPrimary = Color.White,
    primaryContainer = CricketGreenDark,
    onPrimaryContainer = Color.White,
    secondary = MilestoneGold,
    onSecondary = Color.Black,
    secondaryContainer = StadiumNavySurface,
    onSecondaryContainer = MilestoneGoldLight,
    tertiary = DRSElectricBlue,
    onTertiary = Color.White,
    background = StadiumNavyDark,
    onBackground = TextWhitePrimary,
    surface = StadiumNavyCard,
    onSurface = TextWhitePrimary,
    surfaceVariant = StadiumNavySurface,
    onSurfaceVariant = TextMutedSecondary,
    outline = StadiumBorder,
    error = WicketCrimson,
    onError = Color.White
)

private val LightColorScheme = DarkColorScheme // Real Cricket Pro uses a broadcast stadium dark palette for high contrast

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
