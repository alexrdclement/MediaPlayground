package com.alexrdclement.mediaplayground.ui.components.track

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import com.alexrdclement.mediaplayground.media.model.SimpleArtist
import com.alexrdclement.mediaplayground.media.model.SimpleTrack
import com.alexrdclement.mediaplayground.media.model.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.theme.component.media.trackListItem
import com.alexrdclement.mediaplayground.ui.util.PreviewSimpleTrack1
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
import kotlin.time.Duration

data class TrackListItemStyle(
    val contentPadding: PaddingValues = PaddingValues(vertical = 8.dp),
    val leadingContentSize: Dp = 52.dp,
    val playPauseButtonPadding: PaddingValues = PaddingValues(8.dp),
    val textPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    val durationWidth: Dp = 64.dp,
    val disabledContentAlpha: Float = 0.38f,
    val titleStyle: TextStyle = TextStyle(),
    val artistStyle: TextStyle = TextStyle(),
    val trackNumberStyle: TextStyle = TextStyle(),
    val durationStyle: TextStyle = TextStyle(),
    val playPauseButtonStyle: PlayPauseButtonStyle = PlayPauseButtonStyle(),
    val surfaceStyle: SurfaceStyle = SurfaceStyle(),
)

/**
 * A track within an album, where the leading slot falls back to the track number.
 */
@Composable
fun TrackListItem(
    track: SimpleTrack,
    isLoaded: Boolean,
    isPlayable: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TrackListItemStyle = TrackListItemStyle(),
    onLongClick: ((Offset) -> Unit)? = null,
) {
    TrackListItem(
        title = track.name,
        artists = track.artists,
        duration = track.duration,
        isLoaded = isLoaded,
        isPlayable = isPlayable,
        isPlaying = isPlaying,
        onClick = onClick,
        onPlayPauseClick = onPlayPauseClick,
        modifier = modifier,
        style = style,
        onLongClick = onLongClick,
        leadingContent = {
            Text(
                text = track.trackNumber.toString(),
                style = style.trackNumberStyle,
            )
        },
    )
}

/**
 * A track within a playlist, where the leading slot falls back to its artwork.
 */
@Composable
fun TrackListItem(
    item: MediaItemUi,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TrackListItemStyle = TrackListItemStyle(),
    onLongClick: ((Offset) -> Unit)? = null,
) {
    TrackListItem(
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

/**
 * A track within a playlist, where the leading slot falls back to its artwork.
 */
@Composable
fun TrackListItem(
    item: MediaItem,
    isLoaded: Boolean,
    isPlayable: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TrackListItemStyle = TrackListItemStyle(),
    onLongClick: ((Offset) -> Unit)? = null,
) {
    TrackListItem(
        title = item.title,
        artists = item.artists,
        duration = item.duration,
        isLoaded = isLoaded,
        isPlayable = isPlayable,
        isPlaying = isPlaying,
        onClick = onClick,
        onPlayPauseClick = onPlayPauseClick,
        modifier = modifier,
        style = style,
        onLongClick = onLongClick,
        leadingContent = {
            MediaItemArtwork(
                imageUrl = item.thumbnailImageUrl,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
            )
        },
    )
}

@Composable
private fun TrackListItem(
    title: String,
    artists: List<SimpleArtist>,
    duration: Duration,
    isLoaded: Boolean,
    isPlayable: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier,
    style: TrackListItemStyle,
    onLongClick: ((Offset) -> Unit)?,
    leadingContent: @Composable BoxScope.() -> Unit,
) {
    var touchPosition by remember { mutableStateOf(Offset.Zero) }
    Surface(
        onClick = { if (isPlayable) onClick() },
        onLongClick = onLongClick?.let { { it(touchPosition) } },
        enabled = isPlayable || onLongClick != null,
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
                    leadingContent()
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(style.textPadding)
            ) {
                Text(
                    text = title,
                    style = style.titleStyle,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = artistNamesOrDefault(artists),
                    style = style.artistStyle,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
            }
            Text(
                text = remember { duration.formatShort() },
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
private fun TrackPreview() {
    PaletteTheme {
        TrackListItem(
            track = PreviewSimpleTrack1,
            isLoaded = true,
            isPlayable = false,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
            style = PaletteTheme.component.media.trackListItem,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaItemNotLoadedPreview() {
    PaletteTheme {
        TrackListItem(
            item = PreviewTrack1,
            isLoaded = false,
            isPlayable = false,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
            style = PaletteTheme.component.media.trackListItem,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MediaItemLoadedPreview() {
    PaletteTheme {
        TrackListItem(
            item = PreviewTrack1,
            isLoaded = true,
            isPlayable = false,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
            style = PaletteTheme.component.media.trackListItem,
        )
    }
}
