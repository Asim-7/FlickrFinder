package com.example.flickrfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = colorRedDarker,
    onPrimary = Color.White,
    primaryVariant = colorRedDarker,
    secondary = secondColorDark,
    background = Color.DarkGray,
    surface = colorBlack50,
    onSurface = WhiteShadow
)

private val LightColorPalette = lightColors(
    primary = colorRedDark,
    onPrimary = Color.White,
    primaryVariant = colorRedGrayLight,
    secondary = secondColor,
    background = Color.White,
    surface = WhiteShadow,
    onSurface = Color.DarkGray

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
fun FlickrFinderTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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