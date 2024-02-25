package com.alexrdclement.mediaplayground.feature.spotify.navigation

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyLoginActivity
import com.alexrdclement.mediaplayground.feature.spotify.SpotifyLibraryScreen
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track

const val SpotifyLibraryRoute = "spotifyLibrary"

fun NavController.navigateToSpotifyLibrary(navOptions: NavOptions? = null) {
    navigate(SpotifyLibraryRoute, navOptions)
}

fun NavGraphBuilder.spotifyLibraryScreen(
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (Album) -> Unit,
) {
    composable(SpotifyLibraryRoute) {
        val context = LocalContext.current
        SpotifyLibraryScreen(
            onNavigateToLogIn = {
                context.startActivity(Intent(context, SpotifyLoginActivity::class.java))
            },
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToAlbum = onNavigateToAlbum,
        )
    }
}
