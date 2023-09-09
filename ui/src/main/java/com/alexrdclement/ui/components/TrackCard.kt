package com.alexrdclement.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.ui.shared.theme.DisabledAlpha
import com.alexrdclement.ui.shared.util.PreviewTrack1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackCard(
    track: Track,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = remember(track) { track.previewUrl != null }
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
                track.thumbnailImageUrl?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .alpha(if (isEnabled) 1f else DisabledAlpha)
                            .fillMaxSize()
                    )
                }

                PlayButton(
                    isEnabled = isEnabled,
                    onClick = onPlayClick
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(text = track.name)
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
        TrackCard(
            track = PreviewTrack1,
            onPlayClick = {},
            modifier = Modifier.fillMaxWidth(),
            isEnabled = true
        )
    }
}
