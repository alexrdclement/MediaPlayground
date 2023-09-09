package com.alexrdclement.mediaplayground.feature.spotify.navigation

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyLoginActivity
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
        val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
        val savedTracks = viewModel.savedTracks.collectAsLazyPagingItems()
        val context = LocalContext.current
        SpotifyLibraryScreen(
            isLoggedIn = isLoggedIn,
            savedTracks = savedTracks,
            onPlayTrack = { track ->
                viewModel.onPlayTrack(track)
                onPlayTrack(track)
            },
            onLogInClick = {
                context.startActivity(Intent(context, SpotifyLoginActivity::class.java))
            },
            onLogOutClick = viewModel::onLogOutClick,
        )
    }
}
