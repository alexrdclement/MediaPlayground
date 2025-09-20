package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.shared.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.shared.util.artistNamesOrDefault
import com.alexrdclement.uiplayground.components.core.Surface
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.components.media.PlayPauseButton
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

@Composable
fun MediaItemCardTall(
    mediaItem: MediaItem,
    isPlaying: Boolean,
    isPlaybackEnabled: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        border = BorderStroke(1.dp, PlaygroundTheme.colorScheme.outline),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = false)
            ) {
                MediaItemArtwork(
                    imageUrl = mediaItem.thumbnailImageUrl,
                    modifier = Modifier.fillMaxSize()
                )
                PlayPauseButton(
                    isPlaying = isPlaying,
                    isEnabled = isPlaybackEnabled,
                    onClick = onPlayPauseClick,
                    modifier = Modifier
                        .size(24.dp)
                        .align(BiasAlignment(.8f, .8f))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = mediaItem.title,
                    style = PlaygroundTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = artistNamesOrDefault(mediaItem.artists),
                    style = PlaygroundTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PlaygroundTheme {
        MediaItemCardTall(
            mediaItem = PreviewTrack1,
            isPlaybackEnabled = true,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
            modifier = Modifier
                .width(280.dp),
        )
    }
}
