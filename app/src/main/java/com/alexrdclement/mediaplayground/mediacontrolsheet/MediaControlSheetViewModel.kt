package com.alexrdclement.mediaplayground.mediacontrolsheet

import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MediaControlSheetViewModel @Inject constructor(
    mediaSessionManager: MediaSessionManager,
) : ViewModel() {

    val loadedMediaItem = mediaSessionManager.loadedMediaItem
    val isPlaying = mediaSessionManager.isPlaying

    private val player: StateFlow<Player?> = mediaSessionManager.player

    fun onPlayPauseClick() {
        if (!isPlaying.value) {
            player.value?.play()
        } else {
            player.value?.pause()
        }
    }
}
