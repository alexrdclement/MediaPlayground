package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.runtime.Stable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.ui.components.TimeLabeledSeekbar
import com.alexrdclement.mediaplayground.ui.components.TransportControlBar
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack2
import com.alexrdclement.palette.components.core.HorizontalDivider
import com.alexrdclement.palette.components.media.model.Artist
import com.alexrdclement.palette.components.media.model.MediaItem
import com.alexrdclement.palette.components.util.Spacer
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration

@Stable
class MediaControlOverlapState(val maxOverlapPx: Float) {
    var overlapPx by mutableStateOf(0f)
        internal set
    val overlapFraction: Float get() = if (maxOverlapPx > 0f) overlapPx / maxOverlapPx else 0f
}

@Composable
fun MediaControlSheetContent(
    loadedMediaItem: MediaItem,
    playlist: PersistentList<MediaItemUi>,
    transportState: TransportState,
    playheadState: PlayheadState?,
    timelineState: TimelineState?,
    playbackRateState: PlaybackRateState? = null,
    onPlayPauseClick: () -> Unit,
    onPlayPauseLongClick: (() -> Unit)? = null,
    onSkipClick: () -> Unit,
    onSkipBackClick: () -> Unit,
    onSeek: (Duration) -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onNavigateToTrackMetadata: (trackId: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToLoadedItemMetadata: () -> Unit = {},
    onNavigateToLoadedItemDelete: () -> Unit = {},
    onNavigateToArtistMetadata: () -> Unit = {},
    onNavigateToArtistDelete: () -> Unit = {},
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    overlapState: MediaControlOverlapState? = null,
) {
    val nestedScrollConnection = remember(overlapState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val state = overlapState ?: return Offset.Zero
                if (available.y < 0f) {
                    val canConsume = state.maxOverlapPx - state.overlapPx
                    val toConsume = minOf(-available.y, canConsume)
                    if (toConsume > 0f) {
                        state.overlapPx += toConsume
                        return Offset(0f, -toConsume)
                    }
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val state = overlapState ?: return Offset.Zero
                if (available.y > 0f) {
                    val toConsume = minOf(available.y, state.overlapPx)
                    if (toConsume > 0f) {
                        state.overlapPx -= toConsume
                        return Offset(0f, toConsume)
                    }
                }
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Playlist(
            loadedMediaItem = loadedMediaItem,
            playlist = playlist,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            onNavigateToTrackMetadata = onNavigateToTrackMetadata,
            onNavigateToTrackDelete = onNavigateToTrackDelete,
            onNavigateToLoadedItemMetadata = onNavigateToLoadedItemMetadata,
            onNavigateToLoadedItemDelete = onNavigateToLoadedItemDelete,
            onNavigateToArtistMetadata = onNavigateToArtistMetadata,
            onNavigateToArtistDelete = onNavigateToArtistDelete,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .nestedScroll(nestedScrollConnection)
                .layout { measurable, constraints ->
                    val extraHeight = overlapState?.overlapPx?.roundToInt() ?: 0
                    val placeable = measurable.measure(
                        constraints.copy(maxHeight = constraints.maxHeight + extraHeight)
                    )
                    layout(placeable.width, constraints.maxHeight) {
                        placeable.place(0, -extraHeight)
                    }
                },
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PaletteTheme.spacing.medium)
        ) {
            HorizontalDivider()
            Spacer(height = PaletteTheme.spacing.medium)
            TimeLabeledSeekbar(
                playheadState = playheadState,
                timelineState = timelineState,
                transportState = transportState,
                playbackRateState = playbackRateState,
                onSeek = onSeek,
                modifier = Modifier
                    .fillMaxWidth()
            )
            TransportControlBar(
                transportState = transportState,
                onPlayPauseClick = onPlayPauseClick,
                onPlayPauseLongClick = onPlayPauseLongClick,
                onSkipClick = onSkipClick,
                onSkipBackClick = onSkipBackClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PaletteTheme {
        MediaControlSheetContent(
            loadedMediaItem = MediaItem(
                artworkThumbnailUrl = null,
                artworkLargeUrl = null,
                title = "Title",
                artists = listOf(Artist("Artist 1"), Artist("Artist 2"))
            ),
            playlist = persistentListOf(
                MediaItemUi.from(
                    mediaItem = PreviewTrack1,
                    loadedMediaItem = null,
                    isPlaying = false,
                ),
                MediaItemUi.from(
                    mediaItem = PreviewTrack2,
                    loadedMediaItem = null,
                    isPlaying = false,
                ),
            ),
            transportState = TransportState.Stopped,
            playheadState = null,
            timelineState = null,
            onPlayPauseClick = {},
            onSkipClick = {},
            onSkipBackClick = {},
            onSeek = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}
