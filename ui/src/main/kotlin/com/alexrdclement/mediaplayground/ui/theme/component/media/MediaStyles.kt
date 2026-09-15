package com.alexrdclement.mediaplayground.ui.theme.component.media

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.ui.components.MediaItemCardStyle
import com.alexrdclement.mediaplayground.ui.components.MediaItemRowStyle
import com.alexrdclement.mediaplayground.ui.components.PlaylistItemStyle
import com.alexrdclement.mediaplayground.ui.components.TransportControlBarStyle
import com.alexrdclement.mediaplayground.ui.components.track.TrackCardWideStyle
import com.alexrdclement.mediaplayground.ui.components.track.TrackListItemStyle
import com.embarrasdf.palette.components.core.copy
import com.embarrasdf.palette.theme.PaletteTheme
import com.embarrasdf.palette.theme.component.media.MediaStyles

val MediaStyles.transportControlBar: TransportControlBarStyle
    @Composable get() = TransportControlBarStyle(
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        skipButtonSize = PaletteTheme.semantic.dimension.size.touchTargetMin,
        playPauseButtonSize = 72.dp,
        playPauseButtonStyle = playPauseButton,
        skipButtonStyle = skipButton,
    )

val MediaStyles.mediaItemCard: MediaItemCardStyle
    @Composable get() = MediaItemCardStyle(
        contentPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.medium),
        contentSpacing = PaletteTheme.semantic.dimension.spacing.medium,
        textSpacing = PaletteTheme.semantic.dimension.spacing.small,
        playPauseButtonSize = 24.dp,
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
        titleStyle = PaletteTheme.component.core.text.titleMedium,
        itemStyle = mediaItemCard,
        progressIndicatorStyle = PaletteTheme.component.core.progressIndicator,
    )

val MediaStyles.playlistItem: PlaylistItemStyle
    @Composable get() = PlaylistItemStyle(
        contentPadding = PaddingValues(vertical = PaletteTheme.semantic.dimension.spacing.small),
        leadingContentSize = 52.dp,
        playPauseButtonPadding = PaddingValues(PaletteTheme.semantic.dimension.spacing.small),
        textPadding = PaddingValues(horizontal = PaletteTheme.semantic.dimension.spacing.small),
        durationWidth = 64.dp,
        disabledContentAlpha = PaletteTheme.semantic.color.disabledContentAlpha,
        titleStyle = PaletteTheme.component.core.text.titleMedium,
        artistStyle = PaletteTheme.component.core.text.bodyMedium,
        durationStyle = PaletteTheme.component.core.text.bodyMedium.copy(
            textAlign = TextAlign.Center,
        ),
        playPauseButtonStyle = playPauseButton,
        surfaceStyle = PaletteTheme.component.core.surface.default,
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
