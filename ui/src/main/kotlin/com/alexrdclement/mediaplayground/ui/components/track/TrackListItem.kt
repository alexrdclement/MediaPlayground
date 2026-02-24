package com.alexrdclement.mediaplayground.ui.components.track

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.ui.util.PreviewSimpleTrack1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.mediaplayground.ui.util.formatShort
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.components.media.PlayPauseButton
import com.alexrdclement.palette.theme.PaletteTheme
import com.alexrdclement.palette.theme.styles.copy

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
            .padding(vertical = PaletteTheme.spacing.small)
            .alpha(if (isPlayable) 1f else PaletteTheme.colorScheme.disabledContentAlpha)
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
                    style = PaletteTheme.styles.text.bodyMedium.copy(textAlign = TextAlign.Center),
                    modifier = Modifier
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = PaletteTheme.spacing.small)
        ) {
            Text(
                text = track.name,
                style = PaletteTheme.styles.text.titleMedium,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
            Text(
                text = artistNamesOrDefault(track.artists),
                style = PaletteTheme.styles.text.bodyMedium,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
        }
        Text(
            text = remember { track.duration.formatShort() },
            style = PaletteTheme.styles.text.bodyMedium.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(64.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PaletteTheme {
        TrackListItem(
            track = PreviewSimpleTrack1,
            isLoaded = true,
            isPlayable = false,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
        )
    }
}
