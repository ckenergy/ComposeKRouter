package com.ckenergy.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

private val DarkColorPalette = darkColorScheme(
    primary = primaryColor,
    secondary = primaryColor
)

private val LightColorPalette = lightColorScheme(
    primary = primaryColor,
    secondary = primaryColor

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Preview
@Composable
fun ComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}