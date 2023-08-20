package com.alexrdclement.mediaplayground

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexrdclement.mediaplayground.media.service.MediaSessionService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private var mediaController: MutableStateFlow<MediaController?> = MutableStateFlow(null)
    val player: StateFlow<Player?> = mediaController

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _bottomSheet = MutableStateFlow<MainBottomSheet?>(null)
    val bottomSheet = _bottomSheet.asStateFlow()

    init {
        viewModelScope.launch {
            val sessionToken = SessionToken(context, ComponentName(context, MediaSessionService::class.java))
            mediaController.value = MediaController.Builder(context, sessionToken)
                .buildAsync()
                .await()
                .apply(::configureMediaController)
        }
    }

    fun onPickMediaClick() {
        _bottomSheet.update { MainBottomSheet.MediaTypeChooserBottomSheet }
    }

    fun onPickMediaBottomSheetDismiss() {
        _bottomSheet.update { null }
    }

    fun onMediaItemSelected(uri: Uri?) {
        val mediaItem = uri?.let(MediaItem::fromUri)
        mediaItem?.let {
            mediaController.value?.setMediaItem(mediaItem)
        }
    }

    fun onPlayPauseClick() {
        if (!isPlaying.value) {
            mediaController.value?.play()
        } else {
            mediaController.value?.pause()
        }
    }

    internal fun configureMediaController(mediaController: MediaController) = with(mediaController) {
        playWhenReady = true
        addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    _isPlaying.value = isPlaying
                }
            }
        )
    }
}
