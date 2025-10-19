package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeSpotifyAuth @Inject constructor() : SpotifyAuth {

    val mutableAuthState = MutableStateFlow<SpotifyAuthState>(SpotifyAuthState.LoggedOut)
    override val authState: StateFlow<SpotifyAuthState> = mutableAuthState

    override fun logOut() {
        mutableAuthState.update { SpotifyAuthState.LoggedOut }
    }

    fun stubIsLoggedIn(isLoggedIn: Boolean) {
        mutableAuthState.update {
            if (isLoggedIn) SpotifyAuthState.LoggedIn else SpotifyAuthState.LoggedOut
        }
    }
}
