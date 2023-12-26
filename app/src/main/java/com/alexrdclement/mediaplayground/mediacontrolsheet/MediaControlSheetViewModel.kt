package com.alexrdclement.mediaplayground.mediacontrolsheet

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.mediasession.playPause
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaControlSheetViewModel @Inject constructor(
    private val mediaSessionManager: MediaSessionManager,
) : ViewModel() {

    val loadedMediaItem = mediaSessionManager.loadedMediaItem
    val isPlaying = mediaSessionManager.isPlaying

    fun onPlayPauseClick() {
        mediaSessionManager.playPause()
    }
}
