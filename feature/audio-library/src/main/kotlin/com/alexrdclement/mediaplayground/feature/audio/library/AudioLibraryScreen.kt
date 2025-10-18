package com.alexrdclement.mediaplayground.feature.audio.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContent
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentState
import com.alexrdclement.mediaplayground.feature.audio.library.content.spotify.SpotifyContent
import com.alexrdclement.mediaplayground.feature.audio.library.content.spotify.SpotifyContentState
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.alexrdclement.mediaplayground.ui.util.plus
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.components.layout.Scaffold
import com.alexrdclement.uiplayground.components.layout.TopBar
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import kotlinx.coroutines.flow.flowOf

private const val MediaPickerAudioMimeType = "audio/*"

@Composable
fun AudioLibraryScreen(
    onNavigateToSpotifyLogIn: () -> Unit,
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (Album) -> Unit,
    viewModel: AudioLibraryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(AudioLibraryUiState.InitialState)
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) {
        viewModel.onMediaImportItemSelected(it)
    }
    AudioLibraryScreen(
        uiState = uiState,
        onImportClick = {
            mediaPickerLauncher.launch(MediaPickerAudioMimeType)
        },
        onItemClick = { mediaItemUi ->
            viewModel.onItemClick(mediaItemUi)

            when (val mediaItem = mediaItemUi.mediaItem) {
                is Album -> {
                    onNavigateToAlbum(mediaItem)
                }

                is Track -> {
                    if (mediaItem.isPlayable) {
                        onNavigateToPlayer(mediaItem)
                    }
                }
            }
        },
        onItemPlayPauseClick = viewModel::onPlayPauseClick,
        onSpotifyLogInClick = onNavigateToSpotifyLogIn,
        onSpotifyLogOutClick = viewModel::onSpotifyLogOutClick,
    )
}

@Composable
fun AudioLibraryScreen(
    uiState: AudioLibraryUiState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onSpotifyLogInClick: () -> Unit,
    onSpotifyLogOutClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Text(
                        text = "Audio Library",
                        style = PlaygroundTheme.typography.headline,
                    )
                }
            )
        },
    ) { innerPadding ->
        when (uiState) {
            AudioLibraryUiState.InitialState -> {}
            is AudioLibraryUiState.ContentReady -> ContentReady(
                uiState = uiState,
                onImportClick = onImportClick,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
                onLogInClick = onSpotifyLogInClick,
                onLogOutClick = onSpotifyLogOutClick,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
fun ContentReady(
    uiState: AudioLibraryUiState.ContentReady,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentPadding = PaddingValues(horizontal = PlaygroundTheme.spacing.medium).plus(
        horizontal = WindowInsets.navigationBars.asPaddingValues(),
    )
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(PlaygroundTheme.spacing.small),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        LocalContent(
            localContentState = uiState.localContentState,
            onImportClick = onImportClick,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            contentPadding = contentPadding,
        )
        SpotifyContent(
            spotifyContentState = uiState.spotifyContentState,
            onLogInClick = onLogInClick,
            onLogOutClick = onLogOutClick,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            contentPadding = contentPadding,
        )
        Spacer(
            modifier = Modifier
                .mediaControlSheetPadding(isMediaItemLoaded = uiState.isMediaItemLoaded)
                .navigationBarsPadding()
        )
    }
}

@Preview
@Composable
internal fun PreviewLibraryScreen() {
    PlaygroundTheme {
        val uiState = AudioLibraryUiState.ContentReady(
            localContentState = LocalContentState.Content(
                tracks = flowOf(PagingData.from(PreviewTracksUi1)),
                albums = flowOf(PagingData.from(PreviewAlbumsUi1)),
            ),
            spotifyContentState = SpotifyContentState.LoggedIn(
                savedTracks = flowOf(PagingData.from(PreviewTracksUi1)),
                savedAlbums = flowOf(PagingData.from(PreviewAlbumsUi1)),
            ),
            isMediaItemLoaded = false,
        )
        AudioLibraryScreen(
            uiState = uiState,
            onImportClick = {},
            onItemClick = {},
            onItemPlayPauseClick = {},
            onSpotifyLogInClick = {},
            onSpotifyLogOutClick = {},
        )
    }
}
