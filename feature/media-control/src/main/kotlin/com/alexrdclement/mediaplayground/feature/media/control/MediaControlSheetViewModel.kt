package com.alexrdclement.mediaplayground.feature.media.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaControlSheetViewModel @Inject constructor(
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    val loadedMediaItem = mediaSessionState.loadedMediaItem
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    val isPlaying = mediaSessionState.isPlaying
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onPlayPauseClick() {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                transportControl.playPause()
            }
        }
    }
}
