package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl
import com.alexrdclement.mediaplayground.media.engine.TransportControl
import com.alexrdclement.mediaplayground.model.audio.MediaItem

class FakeMediaEngineControl(
    override val mediaEngineState: FakeMediaEngineState = FakeMediaEngineState(),
    override val transportControl: TransportControl = FakeTransportControl(),
    val mediaItemControl: FakeMediaItemControl = FakeMediaItemControl(),
) : MediaEngineControl {

    override suspend fun load(mediaItem: MediaItem) {
        mediaItemControl.load(mediaItem)
    }

    override suspend fun loadFromPlaylist(index: Int) {
        mediaItemControl.loadFromPlaylist(index = index)
    }
}
