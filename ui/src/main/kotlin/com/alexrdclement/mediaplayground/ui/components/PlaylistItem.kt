package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.theme.component.media.playlistItem
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.mediaplayground.ui.util.formatShort
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.media.PlayPauseButton
import com.embarrasdf.palette.components.media.PlayPauseButtonStyle
import com.embarrasdf.palette.theme.PaletteTheme

data class PlaylistItemStyle(
    val contentPadding: PaddingValues = PaddingValues(vertical = 8.dp),
    val leadingContentSize: Dp = 52.dp,
    val playPauseButtonPadding: PaddingValues = PaddingValues(8.dp),
    val textPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    val durationWidth: Dp = 64.dp,
    val disabledContentAlpha: Float = 0.38f,
    val titleStyle: TextStyle = TextStyle(),
    val artistStyle: TextStyle = TextStyle(),
    val durationStyle: TextStyle = TextStyle(),
    val playPauseButtonStyle: PlayPauseButtonStyle = PlayPauseButtonStyle(),
    val surfaceStyle: SurfaceStyle = SurfaceStyle(),
)

@Composable
fun PlaylistItem(
    item: MediaItemUi,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PlaylistItemStyle = PlaylistItemStyle(),
    onLongClick: ((Offset) -> Unit)? = null,
) {
    PlaylistItem(
        item = item.mediaItem,
        isLoaded = item.isLoaded,
        isPlayable = item.isPlayable,
        isPlaying = item.isPlaying,
        onClick = onClick,
        onPlayPauseClick = onPlayPauseClick,
        modifier = modifier,
        style = style,
        onLongClick = onLongClick,
    )
}

@Composable
fun PlaylistItem(
    item: MediaItem,
    isLoaded: Boolean,
    isPlayable: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: PlaylistItemStyle = PlaylistItemStyle(),
    onLongClick: ((Offset) -> Unit)? = null,
) {
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    Surface(
        onClick = onClick,
        onLongClick = onLongClick?.let { { it(touchPosition) } },
        enabled = isPlayable,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false).also { touchPosition = it.position }
                }
            },
        style = style.surfaceStyle,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(style.contentPadding)
                .alpha(if (isPlayable) 1f else style.disabledContentAlpha),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(style.leadingContentSize)
            ) {
                if (isLoaded) {
                    PlayPauseButton(
                        onClick = onPlayPauseClick,
                        isPlaying = isPlaying,
                        isEnabled = isPlayable,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(style.playPauseButtonPadding),
                        style = style.playPauseButtonStyle,
                    )
                } else {
                    MediaItemArtwork(
                        imageUrl = item.thumbnailImageUrl,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxSize()
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(style.textPadding)
            ) {
                Text(
                    text = item.title,
                    style = style.titleStyle,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = artistNamesOrDefault(item.artists),
                    style = style.artistStyle,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
            }
            Text(
                text = remember { item.duration.formatShort() },
                style = style.durationStyle,
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .width(style.durationWidth),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNotLoaded() {
    PaletteTheme {
        PlaylistItem(
            item = PreviewTrack1,
            isLoaded = false,
            isPlayable = false,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
            style = PaletteTheme.component.media.playlistItem,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLoaded() {
    PaletteTheme {
        PlaylistItem(
            item = PreviewTrack1,
            isLoaded = true,
            isPlayable = false,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
            style = PaletteTheme.component.media.playlistItem,
        )
    }
}
