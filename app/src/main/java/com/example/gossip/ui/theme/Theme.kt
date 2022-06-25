package com.example.gossip.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = NightPrimary,
    primaryVariant = NightPrimaryDark,
    secondary = NightPrimaryLight,
    onPrimary = NightText,
    background = NightBackground
)

private val LightColorPalette = lightColors(
    primary = DayPrimary,
    primaryVariant = DayPrimaryDark,
    secondary = DayPrimaryLight,
    onPrimary = DayText,
    background = DayBackground

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun GossipTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}