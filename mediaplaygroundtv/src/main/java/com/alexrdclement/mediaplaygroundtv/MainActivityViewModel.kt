package com.alexrdclement.mediaplaygroundtv

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.data.audio.SpotifyAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val spotifyAuthManager: SpotifyAuthManager,
) : ViewModel() {
    fun onLoginClick(activity: ComponentActivity) {
        spotifyAuthManager.requestLogin(activity)
    }

    fun onNewIntent(intent: Intent) {
        spotifyAuthManager.onNewIntent(intent)
    }
}
