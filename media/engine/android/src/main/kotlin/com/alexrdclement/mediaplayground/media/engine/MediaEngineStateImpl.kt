package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaEngineStateImpl @Inject constructor(
    internal val mediaControllerHolder: MediaControllerHolder,
    private val mediaItemControl: MediaItemState,
    private val transportControl: TransportControl,
) : MediaEngineState {
    override fun getTransportState() = transportControl.getTransportState()
    override fun getLoadedMediaItem() = mediaItemControl.getLoadedMediaItem()
}

fun MediaEngineStateImpl.getPlayer(): Flow<Player?> {
    return flow {
        emit(mediaControllerHolder.getMediaController())
    }
}
