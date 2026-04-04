package com.alexrdclement.mediaplayground.media.ui

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.engine.isPlaying
import kotlinx.coroutines.isActive
import kotlin.time.Duration

@Composable
fun rememberPlayheadPosition(
    playheadState: PlayheadState?,
    transportState: TransportState,
    playbackRateState: PlaybackRateState? = null,
) = produceState(
    initialValue = playheadState?.positionSnapshot ?: Duration.ZERO,
    key1 = playheadState,
    key2 = transportState.isPlaying,
    key3 = playbackRateState?.speed,
) {
    val state = playheadState ?: return@produceState
    if (!transportState.isPlaying) {
        value = state.positionSnapshot
        return@produceState
    }
    val speed = playbackRateState?.speed ?: 1f
    while (isActive) {
        withInfiniteAnimationFrameMillis {
            value = state.positionSnapshot + state.capturedAt.elapsedNow() * speed.toDouble()
        }
    }
}
