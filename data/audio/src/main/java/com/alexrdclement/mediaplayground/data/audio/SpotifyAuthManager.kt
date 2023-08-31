package com.alexrdclement.mediaplayground.data.audio

import android.content.Intent
import androidx.activity.ComponentActivity
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import javax.inject.Inject

private const val ClientId = "6fa9a03a57eb4e43ac157af6744ed5d1"
private const val RedirectUri = "comalexrdclementmediaplayground://callback"
private const val RequestCode = 1929

class SpotifyAuthManager @Inject constructor() {
    fun requestLogin(activity: ComponentActivity) {
        val request = AuthorizationRequest.Builder(
            ClientId,
            AuthorizationResponse.Type.TOKEN,
            RedirectUri
        )
            .setScopes(arrayOf("streaming"))
            .build()

        AuthorizationClient.openLoginInBrowser(activity, request)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode != RequestCode) {
            return
        }
        val response = AuthorizationClient.getResponse(resultCode, intent)
        handleResponse(response)
    }

    fun onNewIntent(intent: Intent) {
        val uri = intent.data ?: return
        val response = AuthorizationResponse.fromUri(uri)
        handleResponse(response)
    }

    private fun handleResponse(response: AuthorizationResponse) {
        println("zzz ${response.type} ${response.state}")
        when (response.type) {
            AuthorizationResponse.Type.CODE -> {

            }
            AuthorizationResponse.Type.TOKEN -> {

            }
            AuthorizationResponse.Type.ERROR -> {

            }
            AuthorizationResponse.Type.EMPTY -> {

            }
            AuthorizationResponse.Type.UNKNOWN -> {

            }
        }
    }
}
