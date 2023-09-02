package com.alexrdclement.mediaplayground.navigation

import com.alexrdclement.mediaplayground.feature.spotify.navigation.SpotifyLibraryRoute

sealed class Destination(val route: String) {
    data object Main : Destination("main")
    data object SpotifyLibrary : Destination(SpotifyLibraryRoute)
}
