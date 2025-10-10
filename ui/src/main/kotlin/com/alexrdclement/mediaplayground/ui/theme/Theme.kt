package com.alexrdclement.mediaplayground.ui.theme

import androidx.compose.ui.graphics.Color

sealed interface ColorScheme {
    val primary: Color
    val secondary: Color
    val tertiary: Color
    val background: Color
    val surface: Color

    data object Dark : ColorScheme {
        override val primary = Color.White
        override val secondary = Color.White
        override val tertiary = Color.White
        override val background = Color.Black
        override val surface = Color.Black
    }

    data object Light : ColorScheme {
        override val primary = Color.Black
        override val secondary = Color.Black
        override val tertiary = Color.Black
        override val background = Color.White
        override val surface = Color.White
    }
}
