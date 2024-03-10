package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import kotlinx.coroutines.flow.StateFlow

interface SpotifyAuth {
    val isLoggedIn: StateFlow<Boolean>

    fun logOut()
}
