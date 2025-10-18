package com.alexrdclement.mediaplayground.feature.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alexrdclement.mediaplayground.ui.components.MediaItemArtwork
import com.alexrdclement.mediaplayground.ui.components.TitleArtistBlock
import com.alexrdclement.mediaplayground.ui.components.track.TrackListItem
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPaddingValues
import com.alexrdclement.mediaplayground.ui.model.TrackUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbum1
import com.alexrdclement.mediaplayground.ui.util.artistNamesOrDefault
import com.alexrdclement.uiplayground.components.util.plus
import com.alexrdclement.uiplayground.components.core.Surface
import com.alexrdclement.uiplayground.components.media.PlayPauseButton
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
    BoxWithConstraints {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(PlaygroundTheme.spacing.small),
            contentPadding = WindowInsets.safeDrawing.asPaddingValues().plus(
                mediaControlSheetPaddingValues(state.isMediaItemLoaded),
            ),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                MediaItemArtwork(
                    imageUrl = state.imageUrl,
                    modifier = Modifier
                        .heightIn(
                            max = with(LocalDensity.current) {
                                (this@BoxWithConstraints.constraints.maxHeight / 2f).toDp()
                            }
                        )
                )
            }
            item {
                TitleArtistBlock(
                    title = state.title,
                    artists = artistNamesOrDefault(artists = state.artists),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = PlaygroundTheme.spacing.small)
                )
            }
            item {
                PlayPauseButton(
                    isPlaying = state.isAlbumPlaying,
                    isEnabled = state.isAlbumPlayable,
                    onClick = onAlbumPlayPauseClick,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(vertical = PlaygroundTheme.spacing.small)
                )
            }
            items(
                state.tracks,
                key = { it.track.id.value }
            ) { trackUi ->
                TrackListItem(
                    track = trackUi.track,
                    isLoaded = trackUi.isLoaded,
                    isPlayable = trackUi.isPlayable,
                    isPlaying = trackUi.isPlaying,
                    onClick = { onTrackClick(trackUi) },
                    onPlayPauseClick = { onTrackPlayPauseClick(trackUi) },
                )
            }
        }
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
