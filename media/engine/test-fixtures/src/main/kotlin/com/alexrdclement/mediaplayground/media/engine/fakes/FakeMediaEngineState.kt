package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.PlaybackPitchState
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import kotlinx.coroutines.flow.Flow

class FakeMediaEngineState(
    val transportControl: FakeTransportControl = FakeTransportControl(),
    val playlistControl: FakePlaylistControl = FakePlaylistControl(),
    val playheadControl: FakePlayheadControl = FakePlayheadControl(),
    val timelineControl: FakeTimelineControl = FakeTimelineControl(),
    val playbackRateControl: FakePlaybackRateControl = FakePlaybackRateControl(),
    val playbackPitchControl: FakePlaybackPitchControl = FakePlaybackPitchControl(),
) : MediaEngineState {

    override val playlistState = playlistControl.playlistState

    override fun getTransportState(): Flow<TransportState> = transportControl.getTransportState()

    override fun getPlayheadState(): Flow<PlayheadState> = playheadControl.getPlayheadState()

    override fun getTimelineState(): Flow<TimelineState> = timelineControl.getTimelineState()

    override fun getPlaybackRateState(): Flow<PlaybackRateState> = playbackRateControl.getPlaybackRateState()

    override fun getPlaybackPitchState(): Flow<PlaybackPitchState> = playbackPitchControl.getPlaybackPitchState()
}
