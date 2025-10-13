package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack2
import com.alexrdclement.mediaplayground.ui.util.calculateHorizontalPadding
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.components.media.PlayPauseButton
import com.alexrdclement.uiplayground.components.media.model.Artist
import com.alexrdclement.uiplayground.components.media.model.MediaItem
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun MediaControlSheetContent(
    loadedMediaItem: MediaItem,
    playlist: PersistentList<MediaItemUi>,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(PlaygroundTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = contentPadding.calculateHorizontalPadding())
    ) {
        item {
            PlayPauseButton(
                isPlaying = isPlaying,
                onClick = onPlayPauseClick,
                modifier = Modifier
                    .size(72.dp)
            )
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(PlaygroundTheme.spacing.small),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(vertical = PlaygroundTheme.spacing.small)
            ) {
                Text(
                    text = loadedMediaItem.title,
                    style = PlaygroundTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = loadedMediaItem.artists.joinToString { it.name },
                    style = PlaygroundTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
            }
        }

        items(
            items = playlist,
            key = { item -> item.mediaItem.id.value },
        ) { item ->
            PlaylistItem(
                item = item,
                onClick = { onItemClick(item) },
                onPlayPauseClick = { onItemPlayPauseClick(item) },
                modifier = Modifier
                    .padding(horizontal = PlaygroundTheme.spacing.medium)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PlaygroundTheme {
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
            isPlaying = false,
            onItemClick = {},
            onPlayPauseClick = {},
            onItemPlayPauseClick = {},
        )
    }
}
