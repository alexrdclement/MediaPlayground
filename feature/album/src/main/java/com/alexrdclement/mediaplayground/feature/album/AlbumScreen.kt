package com.alexrdclement.mediaplayground.feature.album

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl
import com.alexrdclement.ui.components.MediaItemArtwork
import com.alexrdclement.ui.shared.theme.DisabledAlpha
import com.alexrdclement.ui.shared.util.PreviewAlbum1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlbumScreen(
    album: Album?,
    onPlayTrack: (SimpleTrack) -> Unit,
) {
    val verticalScrollState = rememberScrollState()
    Surface {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(verticalScrollState)
                .fillMaxSize()
        ) {
            if (album == null) {
                return@Column
            }
            MediaItemArtwork(
                imageUrl = album.largeImageUrl,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = album.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
                Text(
                    text = album.artists.joinToString { it.name },
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .basicMarquee()
                )
            }
            TrackList(
                tracks = album.tracks,
                onPlayTrack = onPlayTrack,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun TrackList(
    tracks: List<SimpleTrack>,
    onPlayTrack: (SimpleTrack) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        for (track in tracks) {
            TrackListItem(
                track = track,
                isPlayable = track.previewUrl != null,
                onPlayClick = { onPlayTrack(track) },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrackListItem(
    track: SimpleTrack,
    isPlayable: Boolean,
    onPlayClick: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isPlayable) { onPlayClick() }
            .padding(vertical = 8.dp)
            .alpha(if (isPlayable) 1f else DisabledAlpha)
    ) {
        Text(
            text = track.trackNumber.toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(48.dp),
        )
        Column(
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

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        AlbumScreen(
            album = PreviewAlbum1,
            onPlayTrack = {}
        )
    }
}
