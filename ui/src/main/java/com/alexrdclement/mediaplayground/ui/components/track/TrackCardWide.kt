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
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.shared.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.theme.MediaPlaygroundTheme
import com.alexrdclement.uiplayground.components.PlayPauseButton

@Composable
fun TrackCardWide(
    track: Track,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = remember(track) { track.uri != null }
) {
    OutlinedCard(
        enabled = isEnabled,
        onClick = onPlayClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
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
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = track.title)
                Text(text = track.artists.joinToString { it.name })
                Text(text = track.simpleAlbum.name)
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        TrackCardWide(
            track = PreviewTrack1,
            onPlayClick = {},
            modifier = Modifier.fillMaxWidth(),
            isEnabled = true
        )
    }
}
