package com.alexrdclement.mediaplayground.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.alexrdclement.mediaplayground.feature.spotify.navigation.navigateToSpotifyLibrary
import com.alexrdclement.mediaplayground.feature.spotify.navigation.spotifyLibraryScreen
import com.alexrdclement.mediaplayground.feature.player.navigation.navigateToPlayer
import com.alexrdclement.mediaplayground.feature.player.navigation.playerScreen
import com.alexrdclement.ui.components.MediaSource
import com.alexrdclement.ui.components.MediaSourcePickerBottomSheet

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MediaPlaygroundNavHost(
    contentPadding: PaddingValues,
) {
    val navController = rememberNavController()

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
                navController.navigateToPlayer()
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
