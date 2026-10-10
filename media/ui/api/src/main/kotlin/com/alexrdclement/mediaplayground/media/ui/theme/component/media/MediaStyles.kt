package com.alexrdclement.mediaplayground.media.ui.theme.component.media

import androidx.compose.runtime.Composable
import com.alexrdclement.mediaplayground.media.ui.SeekbarStyle
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.media.MediaStyles

val MediaStyles.seekbar: SeekbarStyle
    @Composable get() = SeekbarStyle(
        sliderStyle = PaletteTheme.component.core.slider,
    )
