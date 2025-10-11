package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidMediaEngineState @Inject constructor(
    override val playlistState: PlaylistState,
    internal val mediaControllerHolder: MediaControllerHolder,
    private val transportControl: TransportControl,
) : MediaEngineState {
    override fun getTransportState() = transportControl.getTransportState()
}

suspend fun AndroidMediaEngineState.getPlayer(): Player {
    return mediaControllerHolder.getMediaController()
}
