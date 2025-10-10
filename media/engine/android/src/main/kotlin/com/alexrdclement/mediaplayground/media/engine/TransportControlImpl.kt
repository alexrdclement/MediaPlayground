package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class TransportControlImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
): TransportControl {

    override fun getTransportState(): Flow<TransportState> {
        return callbackFlow {
            val mediaController = mediaControllerHolder.getMediaController()

            val initial = mapPlaybackState(
                state = mediaController.playbackState,
                playWhenReady = mediaController.playWhenReady,
            )
            send(initial)

            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    val state = mapPlaybackState(playbackState, playWhenReady = mediaController.playWhenReady)
                    trySend(state)
                }

                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    val state = mapPlaybackState(mediaController.playbackState, playWhenReady = playWhenReady)
                    trySend(state)
                }
            }
            mediaController.addListener(listener)

            awaitClose {
                mediaController.removeListener(listener)
            }
        }
    }

    override suspend fun play() {
        mediaControllerHolder.getMediaController().play()
    }

    override suspend fun pause() {
        mediaControllerHolder.getMediaController().pause()
    }

    override suspend fun stop() {
        mediaControllerHolder.getMediaController().stop()
    }

    private fun mapPlaybackState(state: Int, playWhenReady: Boolean): TransportState {
        return when (state) {
            Player.STATE_IDLE -> TransportState.Stopped
            Player.STATE_BUFFERING -> TransportState.Buffering
            Player.STATE_READY -> if (playWhenReady) {
                TransportState.Playing
            } else {
                TransportState.Paused
            }
            Player.STATE_ENDED -> TransportState.Stopped
            else -> TransportState.Stopped
        }
    }
}
