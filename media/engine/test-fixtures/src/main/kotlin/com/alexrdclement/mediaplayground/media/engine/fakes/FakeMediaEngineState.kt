package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.PlaybackState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import kotlinx.coroutines.flow.Flow

class FakeMediaEngineState(
    val transportControl: FakeTransportControl = FakeTransportControl(),
    val playlistControl: FakePlaylistControl = FakePlaylistControl(),
    val playheadControl: FakePlayheadControl = FakePlayheadControl(),
    val timelineControl: FakeTimelineControl = FakeTimelineControl(),
    val playbackControl: FakePlaybackControl = FakePlaybackControl(),
) : MediaEngineState {

    override val playlistState = playlistControl.playlistState

    override fun getTransportState(): Flow<TransportState> = transportControl.getTransportState()

    override fun getPlayheadState(): Flow<PlayheadState> = playheadControl.getPlayheadState()

    override fun getTimelineState(): Flow<TimelineState> = timelineControl.getTimelineState()

    override fun getPlaybackState(): Flow<PlaybackState> = playbackControl.getPlaybackState()
}
