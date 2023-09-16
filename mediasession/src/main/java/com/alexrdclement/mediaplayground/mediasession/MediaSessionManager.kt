package com.alexrdclement.mediaplayground.mediasession

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexrdclement.mediaplayground.mediasession.mapper.toMediaItem
import com.alexrdclement.mediaplayground.mediasession.service.MediaSessionService
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaSessionManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var mediaController: MutableStateFlow<MediaController?> = MutableStateFlow(null)
    val player: StateFlow<Player?> = mediaController

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _loadedMediaItem = MutableStateFlow<MediaItem?>(null)
    val loadedMediaItem = _loadedMediaItem.asStateFlow()

    init {
        coroutineScope.launch {
            createMediaSession()
        }
    }

    fun playTrack(track: Track) {
        val mediaItem = track.toMediaItem()
        val player = player.value ?: return
        with(player) {
            setMediaItem(mediaItem)
            play()
        }
        _loadedMediaItem.value = track
    }

    private suspend fun createMediaSession() {
        val sessionToken = SessionToken(context, ComponentName(context, MediaSessionService::class.java))
        mediaController.value = MediaController.Builder(context, sessionToken)
            .buildAsync()
            .await()
            .apply(::configureMediaController)
    }

    private fun configureMediaController(mediaController: MediaController) = with(mediaController) {
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
