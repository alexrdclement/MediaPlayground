package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PlaybackRateControlImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
) : PlaybackRateControl {

    override fun getPlaybackRateState(): Flow<PlaybackRateState> = callbackFlow {
        val mediaController = mediaControllerHolder.getMediaController()

        send(PlaybackRateState(speed = mediaController.playbackParameters.speed))

        val listener = object : Player.Listener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                trySend(PlaybackRateState(speed = playbackParameters.speed))
            }
        }
        mediaController.addListener(listener)

        awaitClose { mediaController.removeListener(listener) }
    }

    override suspend fun setSpeed(speed: Float) {
        val mediaController = mediaControllerHolder.getMediaController()
        val current = mediaController.playbackParameters
        mediaController.playbackParameters = PlaybackParameters(speed, current.pitch)
    }
}
