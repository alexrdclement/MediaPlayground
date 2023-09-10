package com.alexrdclement.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.ui.shared.theme.DisabledAlpha
import com.alexrdclement.ui.shared.util.PreviewTrack1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TrackCardTall(
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
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f, matchHeightConstraintsFirst = false)
            ) {
                track.thumbnailImageUrl?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .alpha(if (isEnabled) 1f else DisabledAlpha)
                            .fillMaxSize()
                    )
                }

                PlayPauseButton(
                    isEnabled = isEnabled,
                    onClick = onPlayClick,
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
                    text = track.name,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = track.artists.joinToString { it.name },
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium,
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
    MediaPlaygroundTheme {
        TrackCardTall(
            track = PreviewTrack1,
            onPlayClick = {},
            isEnabled = true,
            modifier = Modifier
                .width(280.dp),
        )
    }
}
