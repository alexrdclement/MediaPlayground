package com.alexrdclement.mediaplayground.media.ui

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.palette.components.core.Slider
import com.alexrdclement.palette.theme.PaletteTheme
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

@Composable
fun Seekbar(
    transportState: TransportState,
    playheadState: PlayheadState?,
    timelineState: TimelineState?,
    onSeek: (Duration) -> Unit,
    modifier: Modifier = Modifier,
) {
    val timelineDuration = timelineState?.duration
    val isPlaying = transportState == TransportState.Playing

    val currentPosition by produceState(
        initialValue = playheadState?.position ?: Duration.ZERO,
        key1 = playheadState,
        key2 = isPlaying,
    ) {
        val state = playheadState ?: return@produceState
        if (!isPlaying) {
            value = state.position
            return@produceState
        }
        while (isActive) {
            withInfiniteAnimationFrameMillis {
                value = state.position + state.capturedAt.elapsedNow()
            }
        }
    }

    var isDragging by remember { mutableStateOf(false) }
    var isSeeking by remember { mutableStateOf(false) }
    var seekValue by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(playheadState) {
        if (!isDragging) isSeeking = false
    }

    val sliderValue = when {
        isSeeking -> seekValue
        timelineDuration != null && timelineDuration > Duration.ZERO ->
            (currentPosition / timelineDuration).toFloat().coerceIn(0f, 1f)
        else -> 0f
    }

    Slider(
        value = sliderValue,
        onValueChange = { newValue ->
            isDragging = true
            isSeeking = true
            seekValue = newValue
        },
        onValueChangeFinished = {
            isDragging = false
            val seekDuration = timelineDuration ?: run {
                isSeeking = false
                return@Slider
            }
            onSeek(seekDuration * seekValue.toDouble())
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    PaletteTheme {
        Seekbar(
            transportState = TransportState.Playing,
            playheadState = PlayheadState(
                position = 30.seconds,
                capturedAt = TimeSource.Monotonic.markNow(),
            ),
            timelineState = TimelineState(
                duration = 1.minutes,
            ),
            onSeek = {},
        )
    }
}
