package com.alexrdclement.mediaplayground.media.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.palette.components.core.Slider
import com.alexrdclement.palette.theme.PaletteTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeSource

@Composable
fun Seekbar(
    playheadState: PlayheadState?,
    timelineState: TimelineState?,
    transportState: TransportState,
    onSeek: (Duration) -> Unit,
    modifier: Modifier = Modifier,
    playbackRateState: PlaybackRateState? = null,
) {
    val timelineDuration = timelineState?.duration

    val playheadPosition by rememberPlayheadPosition(
        playheadState = playheadState,
        transportState = transportState,
        playbackRateState = playbackRateState,
    )

    Seekbar(
        currentPosition = playheadPosition,
        seekCompletionKey = playheadState,
        timelineDuration = timelineDuration,
        onSeek = onSeek,
        modifier = modifier,
    )
}

@Composable
fun Seekbar(
    currentPosition: Duration,
    seekCompletionKey: Any?,
    timelineDuration: Duration?,
    onSeek: (Duration) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isDragging by remember { mutableStateOf(false) }
    var isSeeking by remember { mutableStateOf(false) }
    var seekValue by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(seekCompletionKey) {
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
private fun SeekbarPreview() {
    PaletteTheme {
        Seekbar(
            playheadState = PlayheadState(
                positionSnapshot = 30.seconds,
                capturedAt = TimeSource.Monotonic.markNow(),
            ),
            timelineState = TimelineState(
                duration = 1.minutes,
            ),
            transportState = TransportState.Stopped,
            onSeek = {},
        )
    }
}
