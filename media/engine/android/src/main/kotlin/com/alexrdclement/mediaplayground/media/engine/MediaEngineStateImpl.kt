package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaEngineStateImpl @Inject constructor(
    override val playlistState: PlaylistState,
    internal val mediaControllerHolder: MediaControllerHolder,
    private val transportControl: TransportControl,
) : MediaEngineState {
    override fun getTransportState() = transportControl.getTransportState()
}

fun MediaEngineStateImpl.getPlayer(): Flow<Player?> {
    return flow {
        emit(mediaControllerHolder.getMediaController())
    }
}
