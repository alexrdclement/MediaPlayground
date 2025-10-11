package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MediaEngineState {
    val playlistState: PlaylistState
    fun getTransportState(): Flow<TransportState>
}

val MediaEngineState.isPlaying: Flow<Boolean>
    get() = getTransportState().map { it == TransportState.Playing }

val MediaEngineState.loadedMediaItem: Flow<MediaItem?>
    get() = playlistState.getLoadedMediaItem()
