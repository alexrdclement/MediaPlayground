package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.PlaylistState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.engine.loadedMediaItem
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

interface MediaSessionState {
    val mediaEngineState: Flow<MediaEngineState>
}

val MediaSessionState.transportState: Flow<TransportState>
    get() = mediaEngineState.flatMapLatest { it.getTransportState() }

val MediaSessionState.playlistState: Flow<PlaylistState>
    get() = mediaEngineState.map { it.playlistState }

val MediaSessionState.loadedMediaItem: Flow<MediaItem?>
    get() = mediaEngineState.flatMapLatest { it.loadedMediaItem }

val MediaSessionState.isPlaying: Flow<Boolean>
    get() = transportState.map { it == TransportState.Playing }
