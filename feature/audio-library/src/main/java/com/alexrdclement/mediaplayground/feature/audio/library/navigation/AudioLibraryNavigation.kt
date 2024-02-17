package com.alexrdclement.mediaplayground.feature.audio.library.navigation

import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyLoginActivity
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryScreen
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem

const val AudioLibraryRoute = "audioLibrary"

fun NavController.navigateToAudioLibrary(navOptions: NavOptions? = null) {
    navigate(AudioLibraryRoute, navOptions)
}

fun NavGraphBuilder.audioLibraryScreen(
    onNavigateToPlayer: (MediaItem) -> Unit,
    onNavigateToAlbum: (Album) -> Unit,
) {
    composable(AudioLibraryRoute) {
        val context = LocalContext.current
        AudioLibraryScreen(
            onNavigateToSpotifyLogIn = {
                context.startActivity(Intent(context, SpotifyLoginActivity::class.java))
            },
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToAlbum = onNavigateToAlbum,
        )
    }
}
