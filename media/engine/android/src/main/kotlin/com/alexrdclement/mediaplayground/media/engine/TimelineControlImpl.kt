package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import dev.zacsweers.metro.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TimelineControlImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
) : TimelineControl {

    override fun getTimelineState(): Flow<TimelineState> = callbackFlow {
        val mediaController = mediaControllerHolder.getMediaController()

        send(currentTimelineState(mediaController))

        val listener = object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                trySend(currentTimelineState(mediaController))
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                trySend(currentTimelineState(mediaController))
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                trySend(currentTimelineState(mediaController))
            }
        }
        mediaController.addListener(listener)

        awaitClose { mediaController.removeListener(listener) }
    }

    private fun currentTimelineState(player: Player): TimelineState = TimelineState(
        duration = player.duration.takeUnless { it == C.TIME_UNSET }?.milliseconds,
    )
}
