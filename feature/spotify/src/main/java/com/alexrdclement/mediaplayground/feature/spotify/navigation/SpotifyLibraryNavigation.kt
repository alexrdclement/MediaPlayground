package com.alexrdclement.mediaplayground.feature.spotify.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
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
        val savedTracks = viewModel.savedTracks.collectAsLazyPagingItems()
        SpotifyLibraryScreen(
            savedTracks = savedTracks,
            onPlayTrack = { track ->
                viewModel.onPlayTrack(track)
                onPlayTrack(track)
          },
        )
    }
}
