package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeSpotifyAuth @Inject constructor() : SpotifyAuth {

    val mutableIsLoggedIn = MutableStateFlow(false)
    override val isLoggedIn: StateFlow<Boolean> = mutableIsLoggedIn

    override fun logOut() {
        mutableIsLoggedIn.update { false }
    }

    fun stubIsLoggedIn(isLoggedIn: Boolean) {
        mutableIsLoggedIn.update { isLoggedIn }
    }
}
