package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource

class PlayheadControlImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
) : PlayheadControl {

    override fun getPlayheadState(): Flow<PlayheadState> = callbackFlow {
        val mediaController = mediaControllerHolder.getMediaController()

        send(currentPlayheadState(mediaController))

        val listener = object : Player.Listener {
            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                trySend(currentPlayheadState(mediaController))
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                trySend(currentPlayheadState(mediaController))
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int,
            ) {
                trySend(currentPlayheadState(mediaController))
            }
        }
        mediaController.addListener(listener)

        awaitClose { mediaController.removeListener(listener) }
    }

    override suspend fun seek(position: Duration) {
        mediaControllerHolder.getMediaController().seekTo(position.inWholeMilliseconds)
    }

    private fun currentPlayheadState(player: Player): PlayheadState = PlayheadState(
        positionSnapshot = player.currentPosition.milliseconds,
        capturedAt = TimeSource.Monotonic.markNow(),
    )
}
