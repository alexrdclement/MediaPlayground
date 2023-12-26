package com.alexrdclement.mediaplayground.feature.album

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.feature.album.AlbumUiState.Success.TrackUi
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.ui.components.MediaItemArtwork
import com.alexrdclement.ui.components.mediacontrolsheet.mediaControlSheetPadding
import com.alexrdclement.ui.shared.theme.DisabledAlpha
import com.alexrdclement.ui.shared.util.PreviewAlbum1
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
import com.alexrdclement.uiplayground.components.PlayPauseButton
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AlbumScreen(
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(AlbumUiState.Loading)
    AlbumScreen(
        uiState = uiState,
        onAlbumPlayPauseClick = viewModel::onAlbumPlayPauseClick,
        onTrackClick = viewModel::onTrackClick,
        onTrackPlayPauseClick = viewModel::onTrackPlayPauseClick,
    )
}

@Composable
fun AlbumScreen(
    uiState: AlbumUiState,
    onAlbumPlayPauseClick: () -> Unit,
    onTrackClick: (TrackUi) -> Unit,
    onTrackPlayPauseClick: (TrackUi) -> Unit,
) {
    Surface {
        when (uiState) {
            AlbumUiState.Loading -> {}
            is AlbumUiState.Success -> LoadedContent(
                state = uiState,
                onAlbumPlayPauseClick = onAlbumPlayPauseClick,
                onTrackClick = onTrackClick,
                onTrackPlayPauseClick = onTrackPlayPauseClick,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LoadedContent(
    state: AlbumUiState.Success,
    onAlbumPlayPauseClick: () -> Unit,
    onTrackClick: (TrackUi) -> Unit,
    onTrackPlayPauseClick: (TrackUi) -> Unit,
) {
    val verticalScrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .statusBarsPadding()
            .mediaControlSheetPadding(isMediaItemLoaded = state.isMediaItemLoaded)
            .verticalScroll(verticalScrollState)
            .fillMaxSize()
    ) {
        MediaItemArtwork(
            imageUrl = state.imageUrl,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = state.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
            Text(
                text = state.artists.joinToString { it.name },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
        }
        PlayPauseButton(
            isPlaying = state.isAlbumPlaying,
            isEnabled = state.isAlbumPlayable,
            onClick = onAlbumPlayPauseClick,
            modifier = Modifier
                .size(72.dp)
        )
        TrackList(
            tracks = state.tracks,
            onTrackClick = onTrackClick,
            onPlayPauseClick = onTrackPlayPauseClick,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .navigationBarsPadding()
        )
    }
}

@Composable
private fun TrackList(
    tracks: List<TrackUi>,
    onTrackClick: (TrackUi) -> Unit,
    onPlayPauseClick: (TrackUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        for (trackUi in tracks) {
            TrackListItem(
                track = trackUi.track,
                isLoaded = trackUi.isLoaded,
                isPlayable = trackUi.isPlayable,
                isPlaying = trackUi.isPlaying,
                onClick = { onTrackClick(trackUi) },
                onPlayPauseClick = { onPlayPauseClick(trackUi) },
            )
        }
    }
}

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

@Preview
@Composable
private fun Preview() {
    MediaPlaygroundTheme {
        val album = PreviewAlbum1
        val tracks = album.tracks.mapIndexed { index, track ->
            TrackUi(
                track = track,
                isLoaded = index == 1,
                isPlaying = index == 1,
                isPlayable = true,
            )
        }
        val uiState = AlbumUiState.Success(
            imageUrl = null,
            title = album.title,
            artists = album.artists,
            tracks = tracks,
            isAlbumPlayable = true,
            isAlbumPlaying = false,
            isMediaItemLoaded = false,
        )
        AlbumScreen(
            uiState = uiState,
            onAlbumPlayPauseClick = {},
            onTrackClick = {},
            onTrackPlayPauseClick = {},
        )
    }
}
