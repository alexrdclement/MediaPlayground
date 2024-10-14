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
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.feature.audio.library.content.AudioLibraryContent
import com.alexrdclement.mediaplayground.ui.components.MediaItemRow
import com.alexrdclement.mediaplayground.ui.components.MediaItemWidthCompact
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.shared.util.PreviewTracksUi1
import com.alexrdclement.mediaplayground.ui.theme.ButtonSpace
import com.alexrdclement.uiplayground.components.Button
import com.alexrdclement.uiplayground.components.Text
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun LocalContent(
    localContentState: LocalContentState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
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
            .padding(vertical = 8.dp),
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
        modifier = Modifier
            .fillMaxSize()
    ) {
        val tracks = localContentState.tracks.collectAsLazyPagingItems()
        val state = rememberLazyListState()

        LaunchedEffect(tracks.itemCount) {
            state.scrollToItem(0)
        }

        MediaItemRow(
            mediaItems = tracks,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Imported tracks",
            lazyListState = state,
            itemWidth = MediaItemWidthCompact,
            contentPadding = contentPadding,
            modifier = Modifier
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
            ),
            onImportClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
        )
    }
}
