package com.alexrdclement.mediaplayground.navigation

import com.alexrdclement.mediaplayground.feature.album.navigation.createAlbumRoute
import com.alexrdclement.mediaplayground.feature.spotify.navigation.SpotifyLibraryRoute
import com.alexrdclement.mediaplayground.feature.player.navigation.PlayerRoute
import com.alexrdclement.mediaplayground.model.audio.AlbumId

sealed class Destination(val route: String) {
    data object Main : Destination("main")
    data object SpotifyLibrary : Destination(SpotifyLibraryRoute)
    data class Album(val id: AlbumId) : Destination(createAlbumRoute(id))
    data object Player : Destination(PlayerRoute)
}
