package com.danidev.apprickmorty.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RickMortyColorScheme = darkColorScheme(
    primary = NeonGreen,
    background = BackgroundDark,
    surface = SearchInputBg,
    onPrimary = ChipActiveText,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    secondary = TextSecondary,
    outline = BorderMuted
)

/** Tema oscuro fijo (el diseño de Figma no contempla modo claro ni dynamicColor). */
@Composable
fun ApprickmortyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RickMortyColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
