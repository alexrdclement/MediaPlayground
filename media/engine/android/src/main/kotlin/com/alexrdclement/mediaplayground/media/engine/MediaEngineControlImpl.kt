package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaEngineControlImpl @Inject constructor(
    override val mediaEngineState: MediaEngineStateImpl,
    override val transportControl: TransportControlImpl,
    private val mediaItemControl: MediaItemControl,
) : MediaEngineControl {
    override suspend fun load(mediaItem: MediaItem) {
        mediaItemControl.load(mediaItem)
    }

    override suspend fun loadFromPlaylist(index: Int) {
        mediaItemControl.loadFromPlaylist(index = index)
    }
}
