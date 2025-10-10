package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.TransportControl
import com.alexrdclement.mediaplayground.media.engine.TransportState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakeTransportControl @Inject constructor() : TransportControl {

    val mutableTransportState = MutableSharedFlow<TransportState>(replay = 1)

    override fun getTransportState(): Flow<TransportState> {
        return mutableTransportState
    }

    override suspend fun play() {
        mutableTransportState.emit(TransportState.Playing)
    }

    override suspend fun pause() {
        mutableTransportState.emit(TransportState.Paused)
    }

    override suspend fun stop() {
        mutableTransportState.emit(TransportState.Stopped)
    }
}
