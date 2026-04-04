package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PlaybackPitchControlImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
) : PlaybackPitchControl {

    override fun getPlaybackPitchState(): Flow<PlaybackPitchState> = callbackFlow {
        val mediaController = mediaControllerHolder.getMediaController()

        send(PlaybackPitchState(pitch = mediaController.playbackParameters.pitch))

        val listener = object : Player.Listener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                trySend(PlaybackPitchState(pitch = playbackParameters.pitch))
            }
        }
        mediaController.addListener(listener)

        awaitClose { mediaController.removeListener(listener) }
    }

    override suspend fun setPitch(pitch: Float) {
        val mediaController = mediaControllerHolder.getMediaController()
        val current = mediaController.playbackParameters
        mediaController.playbackParameters = PlaybackParameters(current.speed, pitch)
    }
}
