package com.alexrdclement.mediaplayground.feature.spotify.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexrdclement.mediaplayground.feature.spotify.SpotifyLibraryScreen
import com.alexrdclement.mediaplayground.feature.spotify.SpotifyLibraryViewModel
import com.alexrdclement.mediaplayground.model.audio.Track

const val SpotifyLibraryRoute = "spotifyLibrary"

fun NavController.navigateToSpotifyLibrary(navOptions: NavOptions? = null) {
    navigate(SpotifyLibraryRoute, navOptions)
}

fun NavGraphBuilder.spotifyLibraryScreen(
    onPlayTrack: (Track) -> Unit,
) {
    composable(SpotifyLibraryRoute) {
        val viewModel: SpotifyLibraryViewModel = hiltViewModel()
        val savedTracks by viewModel.savedTracks.collectAsStateWithLifecycle()
        SpotifyLibraryScreen(
            savedTracks = savedTracks,
            onPlayTrack = { track ->
                viewModel.onPlayTrack(track)
                onPlayTrack(track)
          },
        )
    }
}
