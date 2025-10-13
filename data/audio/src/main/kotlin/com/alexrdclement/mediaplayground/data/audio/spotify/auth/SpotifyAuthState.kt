package com.alexrdclement.mediaplayground.data.audio.spotify.auth

sealed class SpotifyAuthState {
    data object LoggedOut : SpotifyAuthState()
    data object LoggingIn : SpotifyAuthState()
    data object LoggedIn : SpotifyAuthState()
    data class Error(val message: String?) : SpotifyAuthState()
}
