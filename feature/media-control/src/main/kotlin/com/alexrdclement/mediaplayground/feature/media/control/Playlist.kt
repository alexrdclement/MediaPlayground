package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.alexrdclement.mediaplayground.ui.components.TitleArtistBlock
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack2
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.palette.components.media.model.Artist
import com.alexrdclement.palette.components.media.model.MediaItem
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun Playlist(
    loadedMediaItem: MediaItem,
    playlist: PersistentList<MediaItemUi>,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onNavigateToTrackMetadata: (trackId: String) -> Unit,
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit,
    onNavigateToLoadedItemMetadata: () -> Unit,
    onNavigateToLoadedItemDelete: () -> Unit,
    onNavigateToArtistMetadata: () -> Unit,
    onNavigateToArtistDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(PaletteTheme.colorScheme.surface)
    ) {
        stickyHeader {
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
                    .background(PaletteTheme.colorScheme.surface)
                    .padding(top = PaletteTheme.spacing.medium)
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
        Playlist(
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
            onItemClick = {},
            onItemPlayPauseClick = {},
            onNavigateToTrackMetadata = {},
            onNavigateToTrackDelete = { _, _ -> },
            onNavigateToLoadedItemMetadata = {},
            onNavigateToLoadedItemDelete = {},
            onNavigateToArtistMetadata = {},
            onNavigateToArtistDelete = {},
        )
    }
}
