package com.alexrdclement.mediaplayground.data.audio.spotify.auth

import android.content.Context
import com.adamratzman.spotify.SpotifyApiOptions
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyScope
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.adamratzman.spotify.models.Token
import com.adamratzman.spotify.spotifyClientPkceApi
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Properties
import javax.inject.Inject

class SpotifyTvAuth @Inject constructor(
    @ApplicationContext context: Context,
    private val credentialStore: SpotifyDefaultCredentialStore,
) {

    private companion object {
        // Workaround for auth on TV. Tokens are manually placed in this file.
        // NEVER check in this file.
        private const val TokensFileName = "tokens.properties"
    }

    private val assets = context.assets

    private val pkceCodeVerifier: String = (0..96).joinToString("") {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random().toString()
    }

    suspend fun logIn() {
        val properties = Properties().apply {
            load(assets.open(TokensFileName))
        }

        val authCode = properties.getProperty("authorizationCode")

        credentialStore.currentSpotifyPkceCodeVerifier = pkceCodeVerifier

//        val api = spotifyClientPkceApi(
//            clientId = ClientId,
//            redirectUri = RedirectUri,
//            authorization = SpotifyUserAuthorization(
//                authorizationCode = authCode,
//                pkceCodeVerifier = credentialStore.currentSpotifyPkceCodeVerifier
//            ),
//            block = {}
//        ).build()
        val api = SpotifyClientApi(
            clientId = ClientId,
            clientSecret = null,
            redirectUri = RedirectUri,
            token = Token.from(
                accessToken = properties.getProperty("accessToken"),
                refreshToken = properties.getProperty("refreshToken"),
                scopes = SpotifyScope.entries
            ),
            spotifyApiOptions = SpotifyApiOptions(),
            usesPkceAuth = true,
            enableDefaultTokenRefreshProducerIfNoneExists = true,
        )

        if (api.token.accessToken.isNotBlank()) {
            credentialStore.spotifyToken = api.token
        }

        credentialStore.setSpotifyApi(api)
    }
}
