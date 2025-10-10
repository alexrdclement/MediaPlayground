package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow

class FakeMediaEngineState(
    val transportControl: FakeTransportControl = FakeTransportControl(),
    val mediaItemControl: FakeMediaItemControl = FakeMediaItemControl(),
) : MediaEngineState {
    override fun getTransportState(): Flow<TransportState> {
        return transportControl.getTransportState()
    }

    override fun getLoadedMediaItem(): Flow<MediaItem?> {
        return mediaItemControl.mediaItemState.getLoadedMediaItem()
    }
}
