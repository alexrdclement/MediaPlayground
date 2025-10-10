package com.alexrdclement.mediaplayground.media.engine

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface TransportControl {
    fun getTransportState(): Flow<TransportState>
    suspend fun play()
    suspend fun pause()
    suspend fun stop()
}

suspend fun TransportControl.playPause() {
    when (getTransportState().first()) {
        TransportState.Playing -> pause()
        else -> play()
    }
}
