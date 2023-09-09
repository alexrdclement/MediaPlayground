package com.alexrdclement.mediaplayground.navigation

import com.alexrdclement.mediaplayground.feature.spotify.navigation.SpotifyLibraryRoute
import com.alexrdclement.mediaplayground.player.navigation.PlayerRoute

sealed class Destination(val route: String) {
    data object Main : Destination("main")
    data object SpotifyLibrary : Destination(SpotifyLibraryRoute)
    data object Player : Destination(PlayerRoute)
}
