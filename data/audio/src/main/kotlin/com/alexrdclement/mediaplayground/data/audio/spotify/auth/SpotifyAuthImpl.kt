package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpotifyAuthImpl @Inject constructor(
    private val credentialStore: SpotifyDefaultCredentialStore,
) : SpotifyAuth {

    // TODO: do this properly
    private val _isLoggedIn = MutableStateFlow(credentialStore.canApiBeRefreshed())
    override val isLoggedIn = _isLoggedIn.asStateFlow()

    override fun logOut() {
        credentialStore.clear()
        _isLoggedIn.value = false
    }

    internal fun onLogin(api: SpotifyClientApi) {
        credentialStore.setSpotifyApi(api)
        _isLoggedIn.value = true
    }
}
