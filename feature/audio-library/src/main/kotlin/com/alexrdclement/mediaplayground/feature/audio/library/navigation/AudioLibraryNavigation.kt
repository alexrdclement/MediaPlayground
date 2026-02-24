package com.alexrdclement.mediaplayground.feature.audio.library.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
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
        AudioLibraryScreen(
            onNavigateToPlayer = onNavigateToPlayer,
            onNavigateToAlbum = onNavigateToAlbum,
        )
    }
}
