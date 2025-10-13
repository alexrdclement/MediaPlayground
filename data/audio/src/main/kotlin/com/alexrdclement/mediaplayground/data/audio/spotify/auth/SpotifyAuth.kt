package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import kotlinx.coroutines.flow.StateFlow

interface SpotifyAuth {
    val authState: StateFlow<SpotifyAuthState>

    fun logOut()
}
