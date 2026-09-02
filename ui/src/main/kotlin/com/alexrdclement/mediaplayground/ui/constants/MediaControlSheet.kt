package com.alexrdclement.mediaplayground.ui.constants

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.alexrdclement.palette.theme.PaletteTheme

/**
 * Collapsed (peek) height of the media control sheet, sourced from the palette theme so it stays
 * in sync with the control bar's minimum content size.
 */
val MediaControlSheetPeekHeight: Dp
    @Composable get() = PaletteTheme.component.media.mediaControlBar.minContentSize.height

@Composable
fun Modifier.mediaControlSheetPadding(isMediaItemLoaded: Boolean): Modifier =
    this.then(
        if (isMediaItemLoaded) {
            Modifier.padding(bottom = MediaControlSheetPeekHeight)
        } else {
            Modifier
        }
    )

@Composable
fun mediaControlSheetPaddingValues(isMediaItemLoaded: Boolean) = PaddingValues(
    bottom = if (isMediaItemLoaded) MediaControlSheetPeekHeight else PaletteTheme.semantic.dimension.spacing.none
)
