package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.auth.pkce.AbstractSpotifyPkceLoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SpotifyLoginActivity : AbstractSpotifyPkceLoginActivity() {

    @Inject
    lateinit var spotifyAuth: SpotifyAuth

    override val clientId = ClientId
    override val redirectUri = RedirectUri
    override val scopes = SpotifyScope.entries

    override fun onSuccess(api: SpotifyClientApi) {
        spotifyAuth.onLogin(api = api)

        finish()
    }

    override fun onFailure(exception: Exception) {
        exception.printStackTrace()

        finish()
    }
}
