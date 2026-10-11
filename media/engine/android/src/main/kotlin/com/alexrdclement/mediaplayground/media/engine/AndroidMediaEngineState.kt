package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import dev.zacsweers.metro.Inject

class AndroidMediaEngineState @Inject constructor(
    override val playlistState: PlaylistState,
    internal val mediaControllerHolder: MediaControllerHolder,
    private val transportControl: TransportControl,
    private val playbackControls: PlaybackControls,
) : MediaEngineState {
    override fun getTransportState() = transportControl.getTransportState()
    override fun getPlayheadState() = playbackControls.playheadControl.getPlayheadState()
    override fun getTimelineState() = playbackControls.timelineControl.getTimelineState()
    override fun getPlaybackRateState() = playbackControls.playbackRateControl.getPlaybackRateState()
    override fun getPlaybackPitchState() = playbackControls.playbackPitchControl.getPlaybackPitchState()
}

suspend fun AndroidMediaEngineState.getPlayer(): Player {
    return mediaControllerHolder.getMediaController()
}
