package com.alexrdclement.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.thumbnailImageUrl
import com.alexrdclement.ui.shared.util.PreviewTrack1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaControlBar(
    mediaItem: MediaItem,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .navigationBarsPadding()
            .height(IntrinsicSize.Min)
    ) {
        MediaItemArtwork(
            mediaItem.thumbnailImageUrl,
            modifier = Modifier
                .size(64.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = mediaItem.title,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
            Text(
                text = mediaItem.artists.joinToString { it.name },
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
        }

        PlayPauseButton(
            onClick = onPlayPauseClick,
            isPlaying = isPlaying,
            modifier = Modifier
                .size(52.dp)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        MediaControlBar(
            mediaItem = PreviewTrack1,
            isPlaying = false,
            onClick = {},
            onPlayPauseClick = {},
        )
    }
}
