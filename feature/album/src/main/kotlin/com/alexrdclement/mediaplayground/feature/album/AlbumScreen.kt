package com.alexrdclement.mediaplayground.feature.album

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.components.track.TrackList
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.shared.model.TrackUi
import com.alexrdclement.mediaplayground.ui.shared.util.PreviewAlbum1
import com.alexrdclement.mediaplayground.ui.shared.util.artistNamesOrDefault
import com.alexrdclement.uiplayground.components.PlayPauseButton
import com.alexrdclement.uiplayground.components.Surface
import com.alexrdclement.uiplayground.components.Text
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

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
                style = PlaygroundTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                maxLines = 1,
                modifier = Modifier
                    .basicMarquee()
            )
            Text(
                text = artistNamesOrDefault(artists = state.artists),
                style = PlaygroundTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
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

@Preview
@Composable
private fun Preview() {
    PlaygroundTheme {
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
