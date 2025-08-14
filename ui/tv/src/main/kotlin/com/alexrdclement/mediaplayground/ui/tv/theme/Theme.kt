package com.alexrdclement.mediaplayground.ui.tv.theme

import androidx.compose.runtime.Composable
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import com.alexrdclement.mediaplayground.ui.shared.theme.ColorScheme.Dark

private val DarkColorScheme = darkColorScheme(
    primary = Dark.primary,
    secondary = Dark.secondary,
    tertiary = Dark.tertiary
)

@Composable
fun MediaPlaygroundTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
