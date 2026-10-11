package com.alexrdclement.mediaplayground.ui.theme.component.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.ui.theme.component.media.seekbar
import com.alexrdclement.mediaplayground.ui.components.MediaItemCardStyle
import com.alexrdclement.mediaplayground.ui.components.MediaItemRowStyle
import com.alexrdclement.mediaplayground.ui.components.TimeLabeledSeekbarStyle
import com.alexrdclement.mediaplayground.ui.components.TitleArtistBlockStyle
import com.alexrdclement.mediaplayground.ui.components.TransportControlBarStyle
import com.alexrdclement.mediaplayground.ui.components.track.TrackCardWideStyle
import com.alexrdclement.mediaplayground.ui.components.track.TrackListItemStyle
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.media.MediaStyles

/**
 * Size of a play/pause button acting as the main playback control on a screen.
 */
val MediaStyles.playPauseButtonSizePrimary: Dp
    @Composable get() = 72.dp

/**
 * Size of a play/pause button overlaid on artwork within a list or grid item.
 */
val MediaStyles.playPauseButtonSizeCompact: Dp
    @Composable get() = 24.dp

/**
 * Narrowest a media item tile may be before an adaptive grid drops a column.
 */
val MediaStyles.mediaItemGridMinItemWidth: Dp
    @Composable get() = 120.dp

val MediaStyles.transportControlBar: TransportControlBarStyle
    @Composable get() = TransportControlBarStyle(
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        skipButtonSize = PaletteTheme.semantic.dimension.size.touchTargetMin,
        playPauseButtonSize = playPauseButtonSizePrimary,
        playPauseButtonStyle = playPauseButton,
        skipButtonStyle = skipButton,
    )

val MediaStyles.mediaItemCard: MediaItemCardStyle
    @Composable get() = MediaItemCardStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        textSpacing = PaletteTheme.semantic.dimension.spacing.small,
        playPauseButtonSize = playPauseButtonSizeCompact,
        titleStyle = PaletteTheme.component.core.text.titleMedium,
        artistStyle = PaletteTheme.component.core.text.bodyMedium,
        playPauseButtonStyle = playPauseButton,
        surfaceStyle = PaletteTheme.component.core.surface.container,
    )

val MediaStyles.mediaItemRow: MediaItemRowStyle
    @Composable get() = MediaItemRowStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.none),
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        itemSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        itemWidth = 200.dp,
        titleStyle = PaletteTheme.component.core.text.titleMedium,
        itemStyle = mediaItemCard,
        progressIndicatorStyle = PaletteTheme.component.core.progressIndicator,
    )

val MediaStyles.trackListItem: TrackListItemStyle
    @Composable get() = TrackListItemStyle(
        contentPadding = PaddingValues(vertical = PaletteTheme.semantic.dimension.spacing.small),
        leadingContentSize = 52.dp,
        playPauseButtonPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.small),
        textPadding = PaddingValues(horizontal = PaletteTheme.semantic.dimension.spacing.small),
        durationWidth = 64.dp,
        disabledContentAlpha = PaletteTheme.semantic.color.disabledContentAlpha,
        titleStyle = PaletteTheme.component.core.text.titleMedium,
        artistStyle = PaletteTheme.component.core.text.bodyMedium,
        trackNumberStyle = PaletteTheme.component.core.text.bodyMedium.copy(
            textAlign = TextAlign.Center,
        ),
        durationStyle = PaletteTheme.component.core.text.bodyMedium.copy(
            textAlign = TextAlign.Center,
        ),
        playPauseButtonStyle = playPauseButton,
        surfaceStyle = PaletteTheme.component.core.surface.default,
    )

val MediaStyles.trackCardWide: TrackCardWideStyle
    @Composable get() = TrackCardWideStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        artworkSize = 64.dp,
        textSpacing = PaletteTheme.semantic.dimension.spacing.small,
        titleStyle = PaletteTheme.component.core.text.bodyMedium,
        artistStyle = PaletteTheme.component.core.text.bodyMedium,
        albumStyle = PaletteTheme.component.core.text.bodyMedium,
        playPauseButtonStyle = playPauseButton,
        surfaceStyle = PaletteTheme.component.core.surface.default,
    )

val MediaStyles.titleArtistBlock: TitleArtistBlockStyle
    @Composable get() = TitleArtistBlockStyle(
        contentSpacing = PaletteTheme.semantic.dimension.spacing.small,
        titleStyle = PaletteTheme.component.core.text.titleLarge.copy(
            textAlign = TextAlign.Center,
        ),
        artistStyle = PaletteTheme.component.core.text.bodyLarge.copy(
            textAlign = TextAlign.Center,
        ),
        indication = PaletteTheme.semantic.indication,
    )

val MediaStyles.timeLabeledSeekbar: TimeLabeledSeekbarStyle
    @Composable get() = TimeLabeledSeekbarStyle(
        contentSpacing = PaletteTheme.semantic.dimension.spacing.small,
        seekbarPadding = PaddingValues(
            horizontal = PaletteTheme.semantic.dimension.spacing.large,
        ),
        labelPadding = PaddingValues(
            horizontal = PaletteTheme.semantic.dimension.spacing.medium,
        ),
        labelStyle = PaletteTheme.component.core.text.bodySmall,
        seekbarStyle = seekbar,
    )
