package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.ui.components.TimeLabeledSeekbar
import com.alexrdclement.mediaplayground.ui.components.TitleArtistBlock
import com.alexrdclement.mediaplayground.ui.components.TransportControlBar
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack2
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.palette.components.media.model.Artist
import com.alexrdclement.palette.components.media.model.MediaItem
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration

@Composable
fun MediaControlSheetContent(
    loadedMediaItem: MediaItem,
    playlist: PersistentList<MediaItemUi>,
    transportState: TransportState,
    playheadState: PlayheadState?,
    timelineState: TimelineState?,
    onPlayPauseClick: () -> Unit,
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
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            var titleMenuExpanded by remember { mutableStateOf(false) }
            var titleMenuOffset by remember { mutableStateOf(Offset.Zero) }
            var artistMenuExpanded by remember { mutableStateOf(false) }
            var artistMenuOffset by remember { mutableStateOf(Offset.Zero) }
            TitleArtistBlock(
                title = loadedMediaItem.title,
                artists = artistNamesOrDefault(artists = loadedMediaItem.artists),
                onTitleLongClick = { offset ->
                    titleMenuOffset = offset
                    titleMenuExpanded = true
                },
                onArtistsLongClick = { offset ->
                    artistMenuOffset = offset
                    artistMenuExpanded = true
                },
                titleOverlay = {
                    TrackContextMenu(
                        expanded = titleMenuExpanded,
                        offset = titleMenuOffset,
                        onDismissRequest = { titleMenuExpanded = false },
                        onNavigateToMetadata = onNavigateToLoadedItemMetadata,
                        onNavigateToDelete = onNavigateToLoadedItemDelete,
                    )
                },
                artistsOverlay = {
                    ArtistContextMenu(
                        expanded = artistMenuExpanded,
                        offset = artistMenuOffset,
                        onDismissRequest = { artistMenuExpanded = false },
                        onNavigateToMetadata = onNavigateToArtistMetadata,
                        onNavigateToDelete = onNavigateToArtistDelete,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = PaletteTheme.spacing.small)
            )
        }
        item {
            TransportControlBar(
                transportState = transportState,
                onPlayPauseClick = onPlayPauseClick,
                onSkipClick = onSkipClick,
                onSkipBackClick = onSkipBackClick,
                modifier = Modifier
                    .padding(
                        top = PaletteTheme.spacing.medium,
                        bottom = PaletteTheme.spacing.small,
                    )
            )
        }
        item {
            TimeLabeledSeekbar(
                playheadState = playheadState,
                timelineState = timelineState,
                transportState = transportState,
                onSeek = onSeek,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = PaletteTheme.spacing.small)
            )
        }
        items(
            items = playlist,
            key = { item -> item.mediaItem.id.value },
        ) { item ->
            var menuExpanded by remember { mutableStateOf(false) }
            var touchOffset by remember { mutableStateOf(Offset.Zero) }
            Box {
                PlaylistItem(
                    item = item,
                    onClick = { onItemClick(item) },
                    onPlayPauseClick = { onItemPlayPauseClick(item) },
                    onLongClick = { offset ->
                        touchOffset = offset
                        menuExpanded = true
                    },
                )
                TrackContextMenu(
                    expanded = menuExpanded,
                    offset = touchOffset,
                    onDismissRequest = { menuExpanded = false },
                    onNavigateToMetadata = { onNavigateToTrackMetadata(item.mediaItem.id.value) },
                    onNavigateToDelete = { onNavigateToTrackDelete(item.mediaItem.id.value, item.mediaItem.title ?: "") },
                )
            }
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
