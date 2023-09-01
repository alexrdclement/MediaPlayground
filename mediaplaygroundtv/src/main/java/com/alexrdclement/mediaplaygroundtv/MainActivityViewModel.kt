package com.alexrdclement.mediaplaygroundtv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyTvAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val spotifyTvAuth: SpotifyTvAuth,
    private val audioRepository: AudioRepository,
) : ViewModel() {

    fun onLoginClick() {
        viewModelScope.launch {
            spotifyTvAuth.logIn()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val savedTracks = audioRepository.getSavedTracks()
            println(savedTracks)
        }
    }
}
