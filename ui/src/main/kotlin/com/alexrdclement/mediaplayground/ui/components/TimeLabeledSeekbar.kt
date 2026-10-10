package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.ui.Seekbar
import com.alexrdclement.mediaplayground.media.ui.rememberPlayheadPosition
import com.alexrdclement.mediaplayground.ui.theme.component.media.timeLabeledSeekbar
import com.alexrdclement.mediaplayground.ui.util.formatShort
import com.embarrasdf.palette.components.core.SliderStyle
import com.embarrasdf.palette.components.core.Text
import com.embarrasdf.palette.components.core.TextStyle
import com.embarrasdf.palette.theme.PaletteTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class TimeLabeledSeekbarStyle(
    val contentSpacing: Dp = 8.dp,
    val seekbarPadding: PaddingValues = PaddingValues(horizontal = 24.dp),
    val labelPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    val labelStyle: TextStyle = TextStyle(),
    val seekbarStyle: SliderStyle = SliderStyle(),
)

@Composable
fun TimeLabeledSeekbar(
    playheadState: PlayheadState?,
    timelineState: TimelineState?,
    transportState: TransportState,
    onSeek: (Duration) -> Unit,
    modifier: Modifier = Modifier,
    style: TimeLabeledSeekbarStyle = TimeLabeledSeekbarStyle(),
    playbackRateState: PlaybackRateState? = null,
) {
    val timelineDuration = timelineState?.duration

    val currentPositionState = rememberPlayheadPosition(
        playheadState = playheadState,
        transportState = transportState,
        playbackRateState = playbackRateState,
    )
    val currentPosition by currentPositionState

    val displayPosition by remember {
        derivedStateOf { currentPositionState.value.inWholeSeconds.seconds }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(style.contentSpacing),
        modifier = modifier
    ) {
        Seekbar(
            currentPosition = currentPosition,
            seekCompletionKey = playheadState,
            timelineDuration = timelineDuration,
            onSeek = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .padding(style.seekbarPadding),
            style = style.seekbarStyle,
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(style.labelPadding)
        ) {
            Text(
                text = displayPosition.formatShort(),
                style = style.labelStyle,
            )
            Text(
                text = remember(timelineState) { (timelineState?.duration ?: Duration.ZERO).formatShort() },
                style = style.labelStyle,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeLabeledSeekbarPreview() {
    PaletteTheme {
        TimeLabeledSeekbar(
            playheadState = null,
            timelineState = null,
            transportState = TransportState.Stopped,
            onSeek = {},
            style = PaletteTheme.component.media.timeLabeledSeekbar,
        )
    }
}
