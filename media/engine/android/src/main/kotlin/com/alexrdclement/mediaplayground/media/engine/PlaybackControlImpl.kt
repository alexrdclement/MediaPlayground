package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PlaybackControlImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
) : PlaybackControl {

    override fun getPlaybackState(): Flow<PlaybackState> = callbackFlow {
        val mediaController = mediaControllerHolder.getMediaController()

        send(mediaController.playbackParameters.toPlaybackState())

        val listener = object : Player.Listener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                trySend(playbackParameters.toPlaybackState())
            }
        }
        mediaController.addListener(listener)

        awaitClose {
            mediaController.removeListener(listener)
        }
    }

    override suspend fun setSpeed(speed: Float) {
        val mediaController = mediaControllerHolder.getMediaController()
        mediaController.playbackParameters = PlaybackParameters(speed, mediaController.playbackParameters.pitch)
    }

    override suspend fun setPitch(pitch: Float) {
        val mediaController = mediaControllerHolder.getMediaController()
        mediaController.playbackParameters = PlaybackParameters(mediaController.playbackParameters.speed, pitch)
    }

    private fun PlaybackParameters.toPlaybackState() = PlaybackState(speed = speed, pitch = pitch)
}
