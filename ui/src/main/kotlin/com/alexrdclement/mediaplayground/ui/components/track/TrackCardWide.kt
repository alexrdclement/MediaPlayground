package com.alexrdclement.mediaplayground.ui.components.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.media.PlayPauseButton
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
fun TrackCardWide(
    track: Track,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = remember(track) { track.uri != null }
) {
    Surface(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onPlayClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(PaletteTheme.spacing.medium),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(64.dp)
            ) {
                MediaItemArtwork(
                    imageUrl = track.thumbnailImageUrl,
                    isEnabled = isEnabled,
                    modifier = Modifier.fillMaxSize()
                )
                PlayPauseButton(
                    isEnabled = isEnabled,
                    onClick = onPlayClick
                )
            }
            Spacer(modifier = Modifier.width(PaletteTheme.spacing.medium))
            Column(
                verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = track.title)
                Text(text = artistNamesOrDefault(track.artists))
                Text(text = track.simpleAlbum.name)
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
            isEnabled = true
        )
    }
}
