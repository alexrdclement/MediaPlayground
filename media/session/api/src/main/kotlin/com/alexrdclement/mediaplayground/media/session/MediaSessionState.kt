package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.PlaybackPitchState
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.PlaylistState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.engine.loadedMediaItem
import com.alexrdclement.mediaplayground.media.model.audio.MediaItem
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

val MediaSessionState.playheadState: Flow<PlayheadState>
    get() = mediaEngineState.flatMapLatest { it.getPlayheadState() }

val MediaSessionState.timelineState: Flow<TimelineState>
    get() = mediaEngineState.flatMapLatest { it.getTimelineState() }

val MediaSessionState.playbackRateState: Flow<PlaybackRateState>
    get() = mediaEngineState.flatMapLatest { it.getPlaybackRateState() }

val MediaSessionState.playbackPitchState: Flow<PlaybackPitchState>
    get() = mediaEngineState.flatMapLatest { it.getPlaybackPitchState() }
