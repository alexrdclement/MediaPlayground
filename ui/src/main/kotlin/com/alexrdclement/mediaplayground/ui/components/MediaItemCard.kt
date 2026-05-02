package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.thumbnailImageUri
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.media.PlayPauseButton
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
fun MediaItemCard(
    mediaItem: MediaItem,
    isPlaying: Boolean,
    isPlaybackEnabled: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: ((Offset) -> Unit)? = null,
) {
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    Surface(
        onClick = onClick,
        onLongClick = onLongClick?.let { { it(touchPosition) } },
        borderStyle = PaletteTheme.styles.border.surface,
        modifier = modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false).also { touchPosition = it.position }
            }
        },
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
                    uri = mediaItem.thumbnailImageUri,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
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
                    style = PaletteTheme.styles.text.titleMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = artistNamesOrDefault(mediaItem.artists),
                    style = PaletteTheme.styles.text.bodyMedium,
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
    PaletteTheme {
        MediaItemCard(
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
