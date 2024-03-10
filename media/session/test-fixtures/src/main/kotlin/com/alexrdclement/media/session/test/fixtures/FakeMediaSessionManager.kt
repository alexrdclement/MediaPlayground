package com.alexrdclement.media.session.test.fixtures

import androidx.media3.common.Player
import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class FakeMediaSessionManager @Inject constructor() : MediaSessionManager {
    val mutablePlayer = MutableStateFlow<Player?>(null)
    override val player: StateFlow<Player?> = mutablePlayer

    val mutableIsPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = mutableIsPlaying

    val mutableLoadedMediaItem = MutableStateFlow<MediaItem?>(null)
    override val loadedMediaItem: StateFlow<MediaItem?> = mutableLoadedMediaItem

    override fun load(mediaItem: MediaItem) {
        mutableLoadedMediaItem.update { mediaItem }
    }

    override fun loadFromPlaylist(index: Int) {

    }

    override fun play() {
        mutableIsPlaying.update { true }
    }

    override fun pause() {
        mutableIsPlaying.update { false }
    }
}
