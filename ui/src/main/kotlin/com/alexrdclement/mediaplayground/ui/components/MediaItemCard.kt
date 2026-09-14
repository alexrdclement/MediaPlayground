package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.theme.component.media.mediaItemCard
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.media.PlayPauseButton
import com.embarrasdf.palette.components.media.PlayPauseButtonStyle
import com.embarrasdf.palette.theme.PaletteTheme

data class MediaItemCardStyle(
    val contentPadding: PaddingValues = PaddingValues(16.dp),
    val contentSpacing: Dp = 16.dp,
    val textSpacing: Dp = 8.dp,
    val playPauseButtonSize: Dp = 24.dp,
    val playPauseButtonAlignment: BiasAlignment = BiasAlignment(.8f, .8f),
    val titleStyle: TextStyle = TextStyle(),
    val artistStyle: TextStyle = TextStyle(),
    val playPauseButtonStyle: PlayPauseButtonStyle = PlayPauseButtonStyle(),
    val surfaceStyle: SurfaceStyle = SurfaceStyle(),
)

@Composable
fun MediaItemCard(
    mediaItem: MediaItem,
    isPlaying: Boolean,
    isPlaybackEnabled: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: MediaItemCardStyle = MediaItemCardStyle(),
    onLongClick: ((Offset) -> Unit)? = null,
) {
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    Surface(
        onClick = onClick,
        onLongClick = onLongClick?.let { { it(touchPosition) } },
        style = style.surfaceStyle,
        modifier = modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false).also { touchPosition = it.position }
            }
        },
    ) {
        Column(
            modifier = Modifier.padding(style.contentPadding),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = false)
            ) {
                MediaItemArtwork(
                    imageUrl = mediaItem.thumbnailImageUrl,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                )
                PlayPauseButton(
                    isPlaying = isPlaying,
                    isEnabled = isPlaybackEnabled,
                    onClick = onPlayPauseClick,
                    modifier = Modifier
                        .size(style.playPauseButtonSize)
                        .align(style.playPauseButtonAlignment),
                    style = style.playPauseButtonStyle,
                )
            }
            Spacer(modifier = Modifier.height(style.contentSpacing))
            Column(
                verticalArrangement = Arrangement.spacedBy(style.textSpacing),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = mediaItem.title,
                    style = style.titleStyle,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = artistNamesOrDefault(mediaItem.artists),
                    style = style.artistStyle,
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
            style = PaletteTheme.component.media.mediaItemCard,
        )
    }
}
