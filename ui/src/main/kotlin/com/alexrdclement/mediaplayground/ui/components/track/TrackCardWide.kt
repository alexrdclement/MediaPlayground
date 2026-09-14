package com.alexrdclement.mediaplayground.ui.components.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.theme.component.media.trackCardWide
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.components.core.SurfaceStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.components.media.PlayPauseButton
import com.embarrasdf.palette.components.media.PlayPauseButtonStyle
import com.embarrasdf.palette.theme.PaletteTheme

data class TrackCardWideStyle(
    val contentPadding: PaddingValues = PaddingValues(16.dp),
    val contentSpacing: Dp = 16.dp,
    val artworkSize: Dp = 64.dp,
    val textSpacing: Dp = 8.dp,
    val titleStyle: TextStyle = TextStyle(),
    val artistStyle: TextStyle = TextStyle(),
    val albumStyle: TextStyle = TextStyle(),
    val playPauseButtonStyle: PlayPauseButtonStyle = PlayPauseButtonStyle(),
    val surfaceStyle: SurfaceStyle = SurfaceStyle(),
)

@Composable
fun TrackCardWide(
    track: Track,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TrackCardWideStyle = TrackCardWideStyle(),
    isEnabled: Boolean = remember(track) { track.uri != null }
) {
    Surface(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onPlayClick,
        style = style.surfaceStyle,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(style.contentPadding),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(style.artworkSize)
            ) {
                MediaItemArtwork(
                    imageUrl = track.thumbnailImageUrl,
                    isEnabled = isEnabled,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxSize()
                )
                PlayPauseButton(
                    isEnabled = isEnabled,
                    onClick = onPlayClick,
                    style = style.playPauseButtonStyle,
                )
            }
            Spacer(modifier = Modifier.width(style.contentSpacing))
            Column(
                verticalArrangement = Arrangement.spacedBy(style.textSpacing),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = track.title, style = style.titleStyle)
                Text(text = artistNamesOrDefault(track.artists), style = style.artistStyle)
                Text(text = track.simpleAlbum.name, style = style.albumStyle)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        TrackCardWide(
            track = PreviewTrack1,
            onPlayClick = {},
            modifier = Modifier.fillMaxWidth(),
            style = PaletteTheme.component.media.trackCardWide,
            isEnabled = true
        )
    }
}
