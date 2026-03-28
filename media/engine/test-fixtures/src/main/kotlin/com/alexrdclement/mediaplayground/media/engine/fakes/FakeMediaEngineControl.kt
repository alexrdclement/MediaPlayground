package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl

class FakeMediaEngineControl(
    override val playlistControl: FakePlaylistControl = FakePlaylistControl(),
    override val transportControl: FakeTransportControl = FakeTransportControl(),
    override val playheadControl: FakePlayheadControl = FakePlayheadControl(),
    override val timelineControl: FakeTimelineControl = FakeTimelineControl(),
    override val mediaEngineState: FakeMediaEngineState = FakeMediaEngineState(
        transportControl = transportControl,
        playlistControl = playlistControl,
        playheadControl = playheadControl,
        timelineControl = timelineControl,
    ),
) : MediaEngineControl
