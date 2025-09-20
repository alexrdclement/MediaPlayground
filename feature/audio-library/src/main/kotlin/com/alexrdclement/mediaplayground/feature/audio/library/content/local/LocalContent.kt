package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContent
import com.alexrdclement.mediaplayground.ui.components.MediaItemRow
import com.alexrdclement.mediaplayground.ui.components.MediaItemWidthCompact
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.shared.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.shared.util.PreviewTracksUi1
import com.alexrdclement.mediaplayground.ui.theme.ButtonSpace
import com.alexrdclement.uiplayground.components.core.Button
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun LocalContent(
    localContentState: LocalContentState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = PlaygroundTheme.spacing.medium),
) {
    AudioLibraryContent(
        headerText = "Imported",
        headerPadding = contentPadding,
        headerAction = {
            when (localContentState) {
                LocalContentState.Empty -> {}
                is LocalContentState.Content -> Button(
                    onClick = onImportClick,
                    contentPadding = ButtonSpace.ContentPaddingCompact,
                    modifier = Modifier.wrapContentSize(),
                ) {
                    Text(
                        text = "Import",
                        style = PlaygroundTheme.typography.bodySmall
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
            .padding(vertical = PlaygroundTheme.spacing.small),
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
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(PlaygroundTheme.spacing.medium),
        modifier = Modifier
            .fillMaxSize()
    ) {
        val albums = localContentState.albums.collectAsLazyPagingItems()
        val albumsLazyListState = rememberLazyListState()
        val tracks = localContentState.tracks.collectAsLazyPagingItems()
        val tracksLazyListState = rememberLazyListState()

        LaunchedEffect(albums.itemCount) {
            albumsLazyListState.scrollToItem(0)
        }
        LaunchedEffect(tracks.itemCount) {
            tracksLazyListState.scrollToItem(0)
        }

        MediaItemRow(
            mediaItems = albums,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Imported albums",
            lazyListState = albumsLazyListState,
            itemWidth = MediaItemWidthCompact,
            contentPadding = contentPadding,
        )
        MediaItemRow(
            mediaItems = tracks,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Imported tracks",
            lazyListState = tracksLazyListState,
            itemWidth = MediaItemWidthCompact,
            contentPadding = contentPadding,
        )
    }
}

@Preview
@Composable
private fun EmptyPreview() {
    PlaygroundTheme {
        LocalContent(
            localContentState = LocalContentState.Empty,
            onImportClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}

@Preview
@Composable
private fun ContentPreview() {
    PlaygroundTheme {
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
