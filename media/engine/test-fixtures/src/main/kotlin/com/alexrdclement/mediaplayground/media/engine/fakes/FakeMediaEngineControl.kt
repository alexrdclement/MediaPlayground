package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl

class FakeMediaEngineControl(
    override val playlistControl: FakePlaylistControl = FakePlaylistControl(),
    override val transportControl: FakeTransportControl = FakeTransportControl(),
    override val playheadControl: FakePlayheadControl = FakePlayheadControl(),
    override val timelineControl: FakeTimelineControl = FakeTimelineControl(),
    override val playbackRateControl: FakePlaybackRateControl = FakePlaybackRateControl(),
    override val playbackPitchControl: FakePlaybackPitchControl = FakePlaybackPitchControl(),
    override val mediaEngineState: FakeMediaEngineState = FakeMediaEngineState(
        transportControl = transportControl,
        playlistControl = playlistControl,
        playheadControl = playheadControl,
        timelineControl = timelineControl,
        playbackRateControl = playbackRateControl,
        playbackPitchControl = playbackPitchControl,
    ),
) : MediaEngineControl
