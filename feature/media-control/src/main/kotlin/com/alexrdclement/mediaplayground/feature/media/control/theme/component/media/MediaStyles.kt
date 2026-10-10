package com.alexrdclement.mediaplayground.feature.media.control.theme.component.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.alexrdclement.mediaplayground.feature.media.control.MediaControlSheetContentStyle
import com.alexrdclement.mediaplayground.ui.theme.component.media.timeLabeledSeekbar
import com.alexrdclement.mediaplayground.ui.theme.component.media.transportControlBar
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.media.MediaStyles

val MediaStyles.mediaControlSheetContent: MediaControlSheetContentStyle
    @Composable get() = MediaControlSheetContentStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
        controlsPadding = PaddingValues(
            bottom = PaletteTheme.semantic.dimension.spacing.medium,
        ),
        controlsSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        dividerStyle = PaletteTheme.component.core.divider,
        seekbarStyle = timeLabeledSeekbar,
        transportControlBarStyle = transportControlBar,
    )
