package com.alexrdclement.mediaplayground.mediasession

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.alexrdclement.mediaplayground.mediasession.mapper.toMediaItem
import com.alexrdclement.mediaplayground.mediasession.service.MediaSessionService
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack
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

    fun load(mediaItem: MediaItem) {
        when (mediaItem) {
            is Album -> loadAlbum(mediaItem)
            is Track -> loadTrack(mediaItem)
        }
    }

    fun loadFromPlaylist(index: Int) {
        val player = player.value ?: return
        if (index >= player.mediaItemCount) {
            // TODO: log error
            return
        }
        player.seekTo(index, 0)
    }

    fun play() {
        player.value?.play()
    }

    fun pause() {
        player.value?.pause()
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

    private fun loadTrack(track: Track) {
        val player = player.value ?: return
        player.setMediaItem(track.toMediaItem())
        _loadedMediaItem.value = track
    }

    private fun loadAlbum(album: Album) {
        val player = player.value ?: return
        player.clearMediaItems()

        val mediaItems = album.tracks.map { simpleTrack ->
            val track = simpleTrack.toTrack(
                simpleAlbum = album.toSimpleAlbum()
            )
            track.toMediaItem()
        }
        player.addMediaItems(0, mediaItems)

        _loadedMediaItem.value = album
    }
}
