package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.feature.audio.library.AlbumContextMenu
import com.alexrdclement.mediaplayground.feature.audio.library.TrackContextMenu
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContent
import com.alexrdclement.mediaplayground.ui.components.MediaItemRow
import com.alexrdclement.mediaplayground.ui.components.MediaItemWidthCompact
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.alexrdclement.palette.components.core.Button
import com.alexrdclement.palette.components.core.ButtonDefaults
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.theme.PaletteTheme
import com.alexrdclement.palette.theme.styles.ButtonStyleToken
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun LocalContent(
    localContentState: LocalContentState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = PaletteTheme.spacing.medium),
    onNavigateToAlbumMetadata: (albumIdValue: String) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackIdValue: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    AudioLibraryContent(
        headerText = "Imported",
        headerPadding = contentPadding,
        headerAction = {
            when (localContentState) {
                LocalContentState.Empty -> {}
                is LocalContentState.Content -> Button(
                    onClick = onImportClick,
                    contentPadding = ButtonDefaults.ContentPaddingDefault,
                    style = ButtonStyleToken.Secondary,
                    modifier = Modifier.wrapContentSize(),
                ) {
                    Text(
                        text = "Import",
                        style = PaletteTheme.styles.text.bodySmall
                    )
                }
            }
        }
    ) {
        when (localContentState) {
            LocalContentState.Empty -> EmptyContent(
                onImportClick = onImportClick,
            )
            is LocalContentState.Content -> Content(
                localContentState = localContentState,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
                onNavigateToAlbumMetadata = onNavigateToAlbumMetadata,
                onNavigateToAlbumDelete = onNavigateToAlbumDelete,
                onNavigateToTrackMetadata = onNavigateToTrackMetadata,
                onNavigateToTrackDelete = onNavigateToTrackDelete,
                contentPadding = contentPadding,
            )
        }
    }
}

@Composable
private fun EmptyContent(
    onImportClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PaletteTheme.spacing.small),
    ) {
        Button(
            onClick = onImportClick,
        ) {
            Text("Import local audio")
        }
    }
}

@Composable
private fun Content(
    localContentState: LocalContentState.Content,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    contentPadding: PaddingValues,
    onNavigateToAlbumMetadata: (albumIdValue: String) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackIdValue: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.medium),
        modifier = Modifier
            .fillMaxSize()
    ) {
        val albums = localContentState.albums.collectAsLazyPagingItems()
        val tracks = localContentState.tracks.collectAsLazyPagingItems()

        MediaItemRow(
            mediaItems = albums,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Imported albums",
            itemWidth = MediaItemWidthCompact,
            contentPadding = contentPadding,
            itemOverlayContent = { mediaItemUi, expanded, offset, onDismiss ->
                AlbumContextMenu(
                    expanded = expanded,
                    offset = offset,
                    onDismissRequest = onDismiss,
                    onNavigateToMetadata = { onNavigateToAlbumMetadata(mediaItemUi.mediaItem.id.value) },
                    onNavigateToDelete = { onNavigateToAlbumDelete(mediaItemUi.mediaItem.id.value, mediaItemUi.mediaItem.title) },
                )
            },
        )
        MediaItemRow(
            mediaItems = tracks,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Imported tracks",
            itemWidth = MediaItemWidthCompact,
            contentPadding = contentPadding,
            itemOverlayContent = { mediaItemUi, expanded, offset, onDismiss ->
                TrackContextMenu(
                    expanded = expanded,
                    offset = offset,
                    onDismissRequest = onDismiss,
                    onNavigateToMetadata = { onNavigateToTrackMetadata(mediaItemUi.mediaItem.id.value) },
                    onNavigateToDelete = { onNavigateToTrackDelete(mediaItemUi.mediaItem.id.value, mediaItemUi.mediaItem.title) },
                )
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    PaletteTheme {
        LocalContent(
            localContentState = LocalContentState.Empty,
            onImportClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ContentPreview() {
    PaletteTheme {
        LocalContent(
            localContentState = LocalContentState.Content(
                tracks = flowOf(PagingData.from(PreviewTracksUi1)),
                albums = flowOf(PagingData.from(PreviewAlbumsUi1)),
            ),
            onImportClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}
