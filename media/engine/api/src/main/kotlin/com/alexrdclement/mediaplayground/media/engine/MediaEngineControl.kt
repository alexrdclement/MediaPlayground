package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.first

interface MediaEngineControl {
    val mediaEngineState: MediaEngineState
    val transportControl: TransportControl
    suspend fun load(mediaItem: MediaItem)
    suspend fun loadFromPlaylist(index: Int)
}

suspend fun MediaEngineControl.loadIfNecessary(mediaItem: MediaItem) {
    if (mediaItem.id != mediaEngineState.getLoadedMediaItem().first()?.id) {
        load(mediaItem)
    }
}

suspend fun MediaEngineControl.playPause() {
    transportControl.playPause()
}
