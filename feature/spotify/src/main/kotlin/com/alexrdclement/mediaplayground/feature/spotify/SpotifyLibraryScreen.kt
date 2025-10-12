package com.alexrdclement.mediaplayground.feature.spotify

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.ui.components.AuthButton
import com.alexrdclement.mediaplayground.ui.components.MediaItemRow
import com.alexrdclement.mediaplayground.ui.components.MediaItemWidthCompact
import com.alexrdclement.mediaplayground.ui.constants.mediaControlSheetPadding
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.mediaplayground.ui.util.PreviewAlbumsUi1
import com.alexrdclement.mediaplayground.ui.util.PreviewTracksUi1
import com.alexrdclement.mediaplayground.ui.util.plus
import com.alexrdclement.uiplayground.components.core.Surface
import com.alexrdclement.uiplayground.components.core.Text
import com.alexrdclement.uiplayground.theme.PlaygroundTheme
import kotlinx.coroutines.flow.flowOf

private val MediaItemWidth = MediaItemWidthCompact

@Composable
fun SpotifyLibraryScreen(
    onNavigateToLogIn: () -> Unit,
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (Album) -> Unit,
    viewModel: SpotifyLibraryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        initialValue = SpotifyLibraryUiState.InitialState
    )
    SpotifyLibraryScreen(
        uiState = uiState,
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
        onLogInClick = onNavigateToLogIn,
        onLogOutClick = viewModel::onLogOutClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyLibraryScreen(
    uiState: SpotifyLibraryUiState,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit,
    onLogInClick: () -> Unit,
    onLogOutClick: () -> Unit,
) {
    val verticalScrollState = rememberScrollState()
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = PlaygroundTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .verticalScroll(verticalScrollState)
                .fillMaxSize()
                .statusBarsPadding()
                .padding(vertical = 16.dp)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Spotify Library",
                        style = PlaygroundTheme.typography.headline,
                    )
                },
                actions = {
                    val isLoggedIn = remember(uiState) { uiState is SpotifyLibraryUiState.LoggedIn }
                    AuthButton(
                        isLoggedIn = isLoggedIn,
                        onClick = {
                            if (isLoggedIn) {
                                onLogOutClick()
                            } else {
                                onLogInClick()
                            }
                        },
                    )
                }
            )
            when (uiState) {
                SpotifyLibraryUiState.InitialState,
                SpotifyLibraryUiState.NotLoggedIn -> {}
                is SpotifyLibraryUiState.LoggedIn -> {
                    LoggedInContent(
                        uiState = uiState,
                        onItemClick = onItemClick,
                        onItemPlayPauseClick = onItemPlayPauseClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoggedInContent(
    uiState: SpotifyLibraryUiState.LoggedIn,
    onItemClick: (MediaItemUi) -> Unit,
    onItemPlayPauseClick: (MediaItemUi) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val savedAlbums = uiState.savedAlbums.collectAsLazyPagingItems()
        val savedTracks = uiState.savedTracks.collectAsLazyPagingItems()
        val contentPadding = PaddingValues(horizontal = PlaygroundTheme.spacing.medium) +
            WindowInsets.navigationBars.asPaddingValues()
        MediaItemRow(
            mediaItems = savedAlbums,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Saved albums",
            contentPadding = contentPadding,
            itemWidth = MediaItemWidth,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(PlaygroundTheme.spacing.medium))
        MediaItemRow(
            mediaItems = savedTracks,
            onItemClick = onItemClick,
            onItemPlayPauseClick = onItemPlayPauseClick,
            title = "Saved tracks",
            contentPadding = contentPadding,
            itemWidth = MediaItemWidth,
            modifier = Modifier
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
private fun Preview() {
    PlaygroundTheme {
        val uiState = SpotifyLibraryUiState.LoggedIn(
            savedTracks = flowOf(PagingData.from(PreviewTracksUi1)),
            savedAlbums = flowOf(PagingData.from(PreviewAlbumsUi1)),
            isMediaItemLoaded = false,
        )
        SpotifyLibraryScreen(
            uiState = uiState,
            onItemClick = {},
            onItemPlayPauseClick = {},
            onLogInClick = {},
            onLogOutClick = {},
        )
    }
}
