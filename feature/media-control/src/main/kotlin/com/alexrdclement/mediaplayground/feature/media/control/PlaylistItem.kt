package com.alexrdclement.mediaplayground.feature.media.control

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
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewTrack1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.mediaplayground.ui.util.formatShort
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.components.media.PlayPauseButton
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

@Composable
fun PlaylistItem(
    item: MediaItemUi,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PlaylistItem(
        item = item.mediaItem,
        isLoaded = item.isLoaded,
        isPlayable = item.isPlayable,
        isPlaying = item.isPlaying,
        onClick = onClick,
        onPlayPauseClick = onPlayPauseClick,
        modifier = modifier,
    )
}

@Composable
fun PlaylistItem(
    item: MediaItem,
    isLoaded: Boolean,
    isPlayable: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clickable(enabled = isPlayable) { onClick() }
            .padding(vertical = PlaygroundTheme.spacing.small)
            .alpha(if (isPlayable) 1f else PlaygroundTheme.colorScheme.disabledContentAlpha)
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
                MediaItemArtwork(
                    imageUrl = item.thumbnailImageUrl,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = PlaygroundTheme.spacing.small)
        ) {
            Text(
                text = item.title,
                style = PlaygroundTheme.typography.titleMedium,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
            Text(
                text = artistNamesOrDefault(item.artists),
                style = PlaygroundTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
        }
        Text(
            text = remember { item.duration.formatShort() },
            style = PlaygroundTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(64.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PlaygroundTheme {
        PlaylistItem(
            item = PreviewTrack1,
            isLoaded = false,
            isPlayable = false,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
        )
    }
}
