package com.alexrdclement.mediaplayground.navigation

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alexrdclement.mediaplayground.MainBottomSheet
import com.alexrdclement.mediaplayground.MainScreen
import com.alexrdclement.mediaplayground.MainViewModel
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyLoginActivity
import com.alexrdclement.mediaplayground.feature.spotify.navigation.navigateToSpotifyLibrary
import com.alexrdclement.mediaplayground.feature.spotify.navigation.spotifyLibraryScreen
import com.alexrdclement.ui.components.MediaSource
import com.alexrdclement.ui.components.MediaSourcePickerBottomSheet

@Composable
fun MediaPlaygroundNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Main.route,
        modifier = Modifier.fillMaxSize()
    ) {
        mainScreen(
            navigateToSpotifyLibrary = navController::navigateToSpotifyLibrary,
        )
        spotifyLibraryScreen(
            onPlayTrack = { track ->
                navController.popBackStack()
            }
        )
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
        val player by viewModel.player.collectAsStateWithLifecycle()
        val isPlaying by viewModel.isPlaying.collectAsStateWithLifecycle()
        val bottomSheet by viewModel.bottomSheet.collectAsStateWithLifecycle()
        val context = LocalContext.current
        MainScreen(
            player = player,
            isPlaying = isPlaying,
            onLogInClick = {
                context.startActivity(Intent(context, SpotifyLoginActivity::class.java))
            },
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
