package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MediaEngineState {
    fun getTransportState(): Flow<TransportState>
    fun getLoadedMediaItem(): Flow<MediaItem?>
}

val MediaEngineState.isPlaying: Flow<Boolean>
    get() = getTransportState().map { it == TransportState.Playing }
