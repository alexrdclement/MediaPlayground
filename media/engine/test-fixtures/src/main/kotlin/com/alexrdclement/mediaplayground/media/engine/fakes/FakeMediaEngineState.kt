package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import kotlinx.coroutines.flow.Flow

class FakeMediaEngineState(
    val transportControl: FakeTransportControl = FakeTransportControl(),
    val playlistControl: FakePlaylistControl = FakePlaylistControl(),
) : MediaEngineState {

    override val playlistState = playlistControl.playlistState

    override fun getTransportState(): Flow<TransportState> {
        return transportControl.getTransportState()
    }
}
