package com.alexrdclement.mediaplayground

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    mediaSessionManager: MediaSessionManager,
) : ViewModel() {

    private val player: StateFlow<Player?> = mediaSessionManager.player

    val isPlaying = mediaSessionManager.isPlaying

    private val _bottomSheet = MutableStateFlow<MainBottomSheet?>(null)
    val bottomSheet = _bottomSheet.asStateFlow()

    fun onPickMediaClick() {
        _bottomSheet.update { MainBottomSheet.MediaSourceChooserBottomSheet }
    }

    fun onPickMediaBottomSheetDismiss() {
        _bottomSheet.update { null }
    }

    fun onMediaItemSelected(uri: Uri?) {
        val mediaItem = uri?.let(MediaItem::fromUri)
        mediaItem?.let {
            player.value?.setMediaItem(mediaItem)
        }
    }

    fun onPlayPauseClick() {
        if (!isPlaying.value) {
            player.value?.play()
        } else {
            player.value?.pause()
        }
    }
}
