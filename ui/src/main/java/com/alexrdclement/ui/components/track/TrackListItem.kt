package com.alexrdclement.ui.components.track

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.ui.shared.theme.DisabledAlpha
import com.alexrdclement.uiplayground.components.PlayPauseButton
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackListItem(
    track: SimpleTrack,
    isLoaded: Boolean,
    isPlayable: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(enabled = isPlayable) { onClick() }
            .padding(vertical = 8.dp)
            .alpha(if (isPlayable) 1f else DisabledAlpha)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(52.dp)
        ) {
            if (isLoaded) {
                PlayPauseButton(
                    onClick = onPlayPauseClick,
                    isPlaying = isPlaying,
                    isEnabled = isPlayable,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                )
            } else {
                Text(
                    text = track.trackNumber.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
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
        // TODO: use real formatter?
        val milliseconds = track.durationMs.milliseconds
        val seconds = (milliseconds.inWholeSeconds % 60).toString()
        Text(
            text = "${milliseconds.inWholeMinutes}:${seconds.padStart(2, '0')}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(64.dp),
        )
    }
}
