package com.alexrdclement.ui.tv.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.CardLayoutDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.StandardCardLayout
import androidx.tv.material3.Text
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.shared.util.PreviewTrack1
import com.alexrdclement.ui.tv.theme.MediaPlaygroundTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TrackCard(
    track: Track,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StandardCardLayout(
        imageCard = { interactionSource ->
            CardLayoutDefaults.ImageCard(
                onClick = onClick,
                interactionSource = interactionSource
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16/9f),
                ) {
                    Image(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
            }
        },
        title = {
            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = track.title,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        subtitle = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = track.artists.joinToString { it.name })
                Text(text = track.simpleAlbum.name)
            }
        },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        TrackCard(
            track = PreviewTrack1,
            onClick = {},
            modifier = Modifier.size(300.dp)
        )
    }
}
