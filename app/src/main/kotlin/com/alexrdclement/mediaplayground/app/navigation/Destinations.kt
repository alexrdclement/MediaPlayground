package com.alexrdclement.mediaplayground.app.navigation

import com.alexrdclement.mediaplayground.feature.album.navigation.createAlbumRoute
import com.alexrdclement.mediaplayground.feature.spotify.navigation.SpotifyLibraryRoute
import com.alexrdclement.mediaplayground.feature.player.navigation.PlayerRoute
import com.alexrdclement.mediaplayground.feature.audio.library.navigation.AudioLibraryRoute
import com.alexrdclement.mediaplayground.model.audio.AlbumId

sealed class Destination(val route: String) {
    data object AudioLibrary : Destination(AudioLibraryRoute)
    data object SpotifyLibrary : Destination(SpotifyLibraryRoute)
    data class Album(val id: AlbumId) : Destination(createAlbumRoute(id))
    data object Player : Destination(PlayerRoute)
}
