package com.alexrdclement.mediaplayground.navigation

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexrdclement.mediaplayground.MainBottomSheet
import com.alexrdclement.mediaplayground.MainScreen
import com.alexrdclement.mediaplayground.MainViewModel
import com.alexrdclement.mediaplayground.feature.album.navigation.albumScreen
import com.alexrdclement.mediaplayground.feature.album.navigation.navigateToAlbum
import com.alexrdclement.mediaplayground.feature.player.navigation.playerScreen
import com.alexrdclement.mediaplayground.feature.spotify.navigation.navigateToSpotifyLibrary
import com.alexrdclement.mediaplayground.feature.spotify.navigation.spotifyLibraryScreen
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.mediacontrolsheet.MediaControlSheetContent
import com.alexrdclement.ui.components.MediaSource
import com.alexrdclement.ui.components.MediaSourcePickerBottomSheet
import com.alexrdclement.uiplayground.components.Artist
import com.alexrdclement.uiplayground.components.MediaControlSheet
import com.alexrdclement.uiplayground.components.rememberMediaControlSheetState
import kotlinx.coroutines.launch
import com.alexrdclement.uiplayground.components.MediaItem as UiMediaItem

@Composable
fun MediaPlaygroundNavHost(
    contentPadding: PaddingValues,
    currentMediaItem: MediaItem?,
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val mediaControlSheetState = rememberMediaControlSheetState()

    BackHandler(enabled = mediaControlSheetState.isExpanded) {
        coroutineScope.launch {
            mediaControlSheetState.partialExpand()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.SpotifyLibrary.route,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .consumeWindowInsets(contentPadding)
    ) {
        mainScreen(
            navigateToSpotifyLibrary = navController::navigateToSpotifyLibrary,
        )
        spotifyLibraryScreen(
            onNavigateToPlayer = { mediaItem ->
                coroutineScope.launch {
                    mediaControlSheetState.expand()
                }
            },
            onNavigateToAlbum = { album ->
                navController.navigateToAlbum(
                    albumId = album.id,
                )
            }
        )
        albumScreen()
        playerScreen()
    }

    AnimatedVisibility(
        visible = currentMediaItem != null,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = Modifier
    ) {
        currentMediaItem?.let { mediaItem ->
            // TODO: temp
            val artists by derivedStateOf { mediaItem.artists.map { Artist(name = it.name) } }
            val uiMediaItem by derivedStateOf { UiMediaItem(title = mediaItem.title, artists = artists) }
            MediaControlSheet(
                mediaItem = uiMediaItem,
                isPlaying = isPlaying,
                onPlayPauseClick = onPlayPauseClick,
                state = mediaControlSheetState,
                onControlBarClick = {
                    coroutineScope.launch {
                        if (mediaControlSheetState.isExpanded) {
                            mediaControlSheetState.partialExpand()
                        } else {
                            mediaControlSheetState.expand()
                        }
                    }
                },
                modifier = Modifier.systemBarsPadding()
            ) {
                Surface {
                    MediaControlSheetContent(
                        mediaItem = uiMediaItem,
                        isPlaying = isPlaying,
                        onPlayPauseClick = onPlayPauseClick,
                        modifier = Modifier.graphicsLayer {
                            alpha = mediaControlSheetState.partialToFullProgress
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun NavGraphBuilder.mainScreen(
    navigateToSpotifyLibrary: () -> Unit,
) {
    composable(Destination.Main.route) {
        val viewModel = hiltViewModel<MainViewModel>()
        val mediaPickerLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            viewModel.onMediaItemSelected(it)
        }
        val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
        val bottomSheet by viewModel.bottomSheet.collectAsStateWithLifecycle()
        MainScreen(
            isPlaying = isPlaying,
            onPickMediaClick = viewModel::onPickMediaClick,
            onPlayPauseClick = viewModel::onPlayPauseClick,
        )

        when (bottomSheet) {
            MainBottomSheet.MediaSourceChooserBottomSheet -> {
                MediaSourcePickerBottomSheet(
                    onDismissRequest = viewModel::onPickMediaBottomSheetDismiss,
                    onMediaSourcePicked = {
                        // TODO: route event through view model
                        when (it) {
                            MediaSource.DeviceAudio -> mediaPickerLauncher.launch("audio/*")
                            MediaSource.DeviceVideo -> mediaPickerLauncher.launch("video/*")
                            MediaSource.Spotify -> {
                                navigateToSpotifyLibrary()
                            }
                        }
                        viewModel.onPickMediaBottomSheetDismiss()
                    }
                )
            }

            null -> {}
        }
    }
}
