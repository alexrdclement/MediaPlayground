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

    private val _authState = MutableStateFlow(getAuthState())
    override val authState = _authState.asStateFlow()

    override fun logOut() {
        credentialStore.clear()
        updateAuthState()
    }

    internal fun onLogin(api: SpotifyClientApi) {
        credentialStore.setSpotifyApi(api)
        updateAuthState()
    }

    private fun updateAuthState() {
        _authState.value = getAuthState()
    }

    private fun getAuthState(): SpotifyAuthState {
        return if (credentialStore.canApiBeRefreshed()) {
            SpotifyAuthState.LoggedIn
        } else {
            SpotifyAuthState.LoggedOut
        }
    }
}
