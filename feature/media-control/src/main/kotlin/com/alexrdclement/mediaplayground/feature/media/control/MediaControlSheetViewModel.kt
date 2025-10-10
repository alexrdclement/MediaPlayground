package com.alexrdclement.mediaplayground.feature.media.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.media.engine.isPlaying
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaControlSheetViewModel @Inject constructor(
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    val loadedMediaItem = mediaSessionState.mediaEngineState.getLoadedMediaItem()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    private val transportState = mediaSessionState.mediaEngineState.getTransportState()
    val isPlaying = transportState
        .map { it.isPlaying }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onPlayPauseClick() {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                playPause()
            }
        }
    }
}
