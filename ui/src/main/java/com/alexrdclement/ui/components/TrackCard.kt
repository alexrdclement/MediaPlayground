package com.alexrdclement.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.shared.util.PreviewTrack1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

@Composable
fun TrackCard(
    track: Track,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                        .border(
                            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                            shape = CircleShape,
                        )
                        .clip(CircleShape)
                        .clickable { onPlayClick() }
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
        )
    }
}
