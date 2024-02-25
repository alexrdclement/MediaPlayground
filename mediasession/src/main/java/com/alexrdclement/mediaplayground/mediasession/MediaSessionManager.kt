package com.alexrdclement.mediaplayground.mediasession

import androidx.media3.common.Player
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.StateFlow

interface MediaSessionManager {
    val player: StateFlow<Player?>

    val isPlaying: StateFlow<Boolean>

    val loadedMediaItem: StateFlow<MediaItem?>

    fun load(mediaItem: MediaItem)

    fun loadFromPlaylist(index: Int)

    fun play()

    fun pause()
}

fun MediaSessionManager.playPause() {
    if (isPlaying.value) {
        pause()
    } else {
        play()
    }
}

fun MediaSessionManager.loadIfNecessary(mediaItem: MediaItem) {
    if (mediaItem.id != loadedMediaItem.value?.id) {
        load(mediaItem)
    }
}
