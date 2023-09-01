package com.alexrdclement.mediaplaygroundtv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyTvAuth
import com.alexrdclement.mediaplayground.model.audio.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val spotifyTvAuth: SpotifyTvAuth,
    private val audioRepository: AudioRepository,
) : ViewModel() {

    private val _savedTracks = MutableStateFlow<List<Track>>(listOf())
    val savedTracks = _savedTracks.asStateFlow()

    fun onLoginClick() {
        viewModelScope.launch {
            spotifyTvAuth.logIn()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _savedTracks.value = audioRepository.getSavedTracks()
        }
    }
}
