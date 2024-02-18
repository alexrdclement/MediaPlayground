package com.alexrdclement.mediaplayground.feature.audio.library

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryUiState.ContentReady.LocalContentState
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryUiState.ContentReady.SpotifyContentState
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.components.mediacontrolsheet.mediaControlSheetPadding
import com.alexrdclement.ui.components.spotify.SpotifyAuthButton
import com.alexrdclement.ui.components.spotify.SpotifyAuthButtonStyle
import com.alexrdclement.ui.shared.model.MediaItemUi
import com.alexrdclement.ui.shared.util.PreviewAlbumsUi1
import com.alexrdclement.ui.shared.util.PreviewTracksUi1
import com.alexrdclement.ui.theme.ButtonSpace
import com.alexrdclement.ui.theme.MediaPlaygroundTheme
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
        ActivityResultContracts.GetContent()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioLibraryScreen(
    uiState: AudioLibraryUiState,
    onImportClick: () -> Unit,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onSpotifyLogInClick: () -> Unit,
    onSpotifyLogOutClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Audio Library",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                },
            )
            when (uiState) {
                AudioLibraryUiState.InitialState -> {}
                is AudioLibraryUiState.ContentReady -> ContentReady(
                    uiState = uiState,
                    onImportClick = onImportClick,
                    onItemClick = onItemClick,
                    onItemPlayPauseClick = onItemPlayPauseClick,
                    onLogInClick = onSpotifyLogInClick,
                    onLogOutClick = onSpotifyLogOutClick,
                )
            }
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
) {
    val contentPadding = PaddingValues(horizontal = 16.dp)
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .mediaControlSheetPadding(isMediaItemLoaded = uiState.isMediaItemLoaded)
            .verticalScroll(scrollState)
    ) {
        AudioLibraryContent(
            headerText = "Spotify",
            headerPadding = contentPadding,
            headerAction = {
                when (uiState.spotifyContentState) {
                    is SpotifyContentState.LoggedIn -> {
                        SpotifyAuthButton(
                            style = SpotifyAuthButtonStyle.Compact,
                            isLoggedIn = true,
                            onClick = onLogOutClick,
                        )
                    }
                    SpotifyContentState.NotLoggedIn -> {}
                }
            },
        ) {
            SpotifyContent(
                spotifyContentState = uiState.spotifyContentState,
                onLogInClick = onLogInClick,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
            )
        }
        AudioLibraryContent(
            headerText = "Imported",
            headerPadding = contentPadding,
            headerAction = {
                when (uiState.localContentState) {
                    LocalContentState.Empty -> {}
                    is LocalContentState.Content -> OutlinedButton(
                        onClick = onImportClick,
                        contentPadding = ButtonSpace.ContentPaddingCompact,
                        modifier = Modifier.wrapContentSize(),
                    ) {
                        Text(
                            text = "Import",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        ) {
            LocalStorageContent(
                localContentState = uiState.localContentState,
                onImportClick = onImportClick,
                onItemClick = onItemClick,
                onItemPlayPauseClick = onItemPlayPauseClick,
            )
        }
    }
}

@Composable
private fun AudioLibraryContent(
    headerText: String,
    headerPadding: PaddingValues = PaddingValues(0.dp),
    headerAction: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 40.dp) // Match min button height
                .padding(headerPadding)
        ) {
            Text(
                text = headerText,
                style = MaterialTheme.typography.titleLarge,
            )
            headerAction?.invoke()
        }
        content()
    }
}

@Preview
@Composable
internal fun PreviewLibraryScreen() {
    MediaPlaygroundTheme {
        val uiState = AudioLibraryUiState.ContentReady(
            localContentState = LocalContentState.Content(
                tracks = flowOf(PagingData.from(PreviewTracksUi1)),
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
