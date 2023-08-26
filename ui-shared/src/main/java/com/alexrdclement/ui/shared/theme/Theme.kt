package com.alexrdclement.ui.shared.theme

import androidx.compose.ui.graphics.Color

sealed interface ColorScheme {
    val primary: Color
    val secondary: Color
    val tertiary: Color

    data object Dark : ColorScheme {
        override val primary = Purple80
        override val secondary = PurpleGrey80
        override val tertiary = Pink80
    }

    data object Light : ColorScheme {
        override val primary = Purple40
        override val secondary = PurpleGrey40
        override val tertiary = Pink40
    }
}
