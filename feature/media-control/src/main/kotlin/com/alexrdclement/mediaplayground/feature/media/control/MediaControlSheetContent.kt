package com.alexrdclement.mediaplayground.feature.media.control

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.uiplayground.components.PlayPauseButton
import com.alexrdclement.uiplayground.components.Text
import com.alexrdclement.uiplayground.components.model.Artist
import com.alexrdclement.uiplayground.components.model.MediaItem
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

@Composable
fun MediaControlSheetContent(
    loadedMediaItem: MediaItem,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        PlayPauseButton(
            isPlaying = isPlaying,
            onClick = onPlayPauseClick,
            modifier = Modifier
                .size(72.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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
            isPlaying = false,
            onPlayPauseClick = {}
        )
    }
}
