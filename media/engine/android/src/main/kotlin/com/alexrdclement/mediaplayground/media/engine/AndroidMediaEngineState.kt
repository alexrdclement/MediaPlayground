package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import dev.zacsweers.metro.Inject

class AndroidMediaEngineState @Inject constructor(
    override val playlistState: PlaylistState,
    internal val mediaControllerHolder: MediaControllerHolder,
    private val transportControl: TransportControl,
    private val playheadControl: PlayheadControl,
    private val timelineControl: TimelineControl,
    private val playbackRateControl: PlaybackRateControl,
    private val playbackPitchControl: PlaybackPitchControl,
) : MediaEngineState {
    override fun getTransportState() = transportControl.getTransportState()
    override fun getPlayheadState() = playheadControl.getPlayheadState()
    override fun getTimelineState() = timelineControl.getTimelineState()
    override fun getPlaybackRateState() = playbackRateControl.getPlaybackRateState()
    override fun getPlaybackPitchState() = playbackPitchControl.getPlaybackPitchState()
}

suspend fun AndroidMediaEngineState.getPlayer(): Player {
    return mediaControllerHolder.getMediaController()
}
