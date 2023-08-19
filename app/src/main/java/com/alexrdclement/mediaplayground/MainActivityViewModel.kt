package com.alexrdclement.mediaplayground

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexrdclement.mediaplayground.media.service.MediaSessionService
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context,
) : ViewModel() {

    private val sessionToken =
        SessionToken(context, ComponentName(context, MediaSessionService::class.java))
    private val mediaControllerFuture: ListenableFuture<MediaController> =
        MediaController.Builder(context, sessionToken)
            .buildAsync()
    private var mediaController: MediaController? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _bottomSheet = MutableStateFlow<MainBottomSheet?>(null)
    val bottomSheet = _bottomSheet.asStateFlow()

    init {
        mediaControllerFuture
            .addListener(
                {
                    this.mediaController = mediaControllerFuture.get()
                        .apply(::configureMediaController)
                },
                MoreExecutors.directExecutor()
            )
    }

    override fun onCleared() {
        MediaController.releaseFuture(mediaControllerFuture)
        super.onCleared()
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
            mediaController?.setMediaItem(mediaItem)
        }
    }

    fun onPlayPauseClick() {
        if (!isPlaying.value) {
            mediaController?.play()
        } else {
            mediaController?.pause()
        }
    }

    internal fun configureMediaController(mediaController: MediaController) = with(mediaController) {
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
