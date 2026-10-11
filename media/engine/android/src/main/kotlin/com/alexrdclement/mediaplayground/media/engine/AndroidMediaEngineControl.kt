package com.alexrdclement.mediaplayground.media.engine

import dev.zacsweers.metro.Inject

class AndroidMediaEngineControl @Inject constructor(
    override val mediaEngineState: AndroidMediaEngineState,
    override val transportControl: TransportControlImpl,
    override val playlistControl: PlaylistControl,
    playbackControls: PlaybackControls,
) : MediaEngineControl {
    override val playheadControl: PlayheadControl = playbackControls.playheadControl
    override val timelineControl: TimelineControl = playbackControls.timelineControl
    override val playbackRateControl: PlaybackRateControl = playbackControls.playbackRateControl
    override val playbackPitchControl: PlaybackPitchControl = playbackControls.playbackPitchControl
}
