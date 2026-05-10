package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.media.model.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MediaEngineState {
    val playlistState: PlaylistState
    fun getTransportState(): Flow<TransportState>
    fun getPlayheadState(): Flow<PlayheadState>
    fun getTimelineState(): Flow<TimelineState>
    fun getPlaybackRateState(): Flow<PlaybackRateState>
    fun getPlaybackPitchState(): Flow<PlaybackPitchState>
}

val MediaEngineState.isPlaying: Flow<Boolean>
    get() = getTransportState().map { it == TransportState.Playing }

val MediaEngineState.loadedMediaItem: Flow<MediaItem?>
    get() = playlistState.getLoadedMediaItem()
