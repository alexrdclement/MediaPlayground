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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.feature.audio.library.AlbumContextMenu
import com.alexrdclement.mediaplayground.feature.audio.library.TrackContextMenu
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContent
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContentStyle
import com.alexrdclement.mediaplayground.feature.audio.library.theme.component.media.localContent
import com.alexrdclement.mediaplayground.ui.components.MediaItemRow
import com.alexrdclement.mediaplayground.ui.components.MediaItemRowStyle
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.embarrasdf.palette.components.core.Button
import com.embarrasdf.palette.components.core.ButtonStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.theme.PaletteTheme
import kotlinx.coroutines.flow.flowOf

data class LocalContentStyle(
    val contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    val headerStyle: AudioLibraryContentStyle = AudioLibraryContentStyle(),
    val importButtonStyle: ButtonStyle = ButtonStyle(),
    val importButtonTextStyle: TextStyle = TextStyle(),
    val emptyContentStyle: LocalEmptyContentStyle = LocalEmptyContentStyle(),
    val mediaContentStyle: LocalMediaContentStyle = LocalMediaContentStyle(),
)

data class LocalEmptyContentStyle(
    val contentPadding: PaddingValues = PaddingValues(vertical = 8.dp),
    val buttonStyle: ButtonStyle = ButtonStyle(),
    val buttonTextStyle: TextStyle = TextStyle(),
)

data class LocalMediaContentStyle(
    val contentSpacing: Dp = 16.dp,
    val itemRowStyle: MediaItemRowStyle = MediaItemRowStyle(),
)

@Composable
internal fun LocalContent(
    localContentState: LocalContentState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    style: LocalContentStyle = LocalContentStyle(),
    onNavigateToAlbumMetadata: (albumIdValue: String) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackIdValue: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    AudioLibraryContent(
        headerText = "Imported",
        style = style.headerStyle.copy(headerPadding = style.contentPadding),
        headerAction = {
            when (localContentState) {
                LocalContentState.Empty -> {}
                is LocalContentState.Content -> Button(
                    onClick = onImportClick,
                    style = style.importButtonStyle,
                    modifier = Modifier.wrapContentSize(),
                ) {
                    Text(
                        text = "Import",
                        style = style.importButtonTextStyle,
                    )
                }
            }
        }
    ) {
        when (localContentState) {
            LocalContentState.Empty -> EmptyContent(
                onImportClick = onImportClick,
                style = style.emptyContentStyle,
            )
            is LocalContentState.Content -> Content(
                localContentState = localContentState,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
                onNavigateToAlbumMetadata = onNavigateToAlbumMetadata,
                onNavigateToAlbumDelete = onNavigateToAlbumDelete,
                onNavigateToTrackMetadata = onNavigateToTrackMetadata,
                onNavigateToTrackDelete = onNavigateToTrackDelete,
                style = style.mediaContentStyle.copy(
                    itemRowStyle = style.mediaContentStyle.itemRowStyle.copy(
                        contentPadding = style.contentPadding,
                    ),
                ),
            )
        }
    }
}

@Composable
private fun EmptyContent(
    onImportClick: () -> Unit,
    style: LocalEmptyContentStyle = LocalEmptyContentStyle(),
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(style.contentPadding),
    ) {
        Button(
            onClick = onImportClick,
            style = style.buttonStyle,
        ) {
            Text("Import local audio", style = style.buttonTextStyle)
        }
    }
}

@Composable
private fun Content(
    localContentState: LocalContentState.Content,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    style: LocalMediaContentStyle = LocalMediaContentStyle(),
    onNavigateToAlbumMetadata: (albumIdValue: String) -> Unit = {},
    onNavigateToAlbumDelete: (albumId: String, displayName: String) -> Unit = { _, _ -> },
    onNavigateToTrackMetadata: (trackIdValue: String) -> Unit = {},
    onNavigateToTrackDelete: (trackId: String, displayName: String) -> Unit = { _, _ -> },
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
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
            style = style.itemRowStyle,
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
            style = style.itemRowStyle,
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
            style = PaletteTheme.component.media.localContent,
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
            style = PaletteTheme.component.media.localContent,
        )
    }
}
