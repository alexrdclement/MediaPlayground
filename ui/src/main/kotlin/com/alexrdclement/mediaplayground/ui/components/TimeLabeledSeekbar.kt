package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.ui.Seekbar
import com.alexrdclement.mediaplayground.media.ui.rememberPlayheadPosition
import com.alexrdclement.mediaplayground.ui.util.formatShort
import com.alexrdclement.palette.components.core.Text
import com.alexrdclement.palette.theme.PaletteTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimeLabeledSeekbar(
    playheadState: PlayheadState?,
    timelineState: TimelineState?,
    transportState: TransportState,
    onSeek: (Duration) -> Unit,
    modifier: Modifier = Modifier,
) {
    val timelineDuration = timelineState?.duration

    val currentPositionState = rememberPlayheadPosition(
        playheadState = playheadState,
        transportState = transportState,
    )
    val currentPosition by currentPositionState

    val displayPosition by remember {
        derivedStateOf { currentPositionState.value.inWholeSeconds.seconds }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(PaletteTheme.spacing.small),
        modifier = modifier
    ) {
        Seekbar(
            currentPosition = currentPosition,
            seekCompletionKey = playheadState,
            timelineDuration = timelineDuration,
            onSeek = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaletteTheme.spacing.large)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaletteTheme.spacing.medium)
        ) {
            Text(
                text = displayPosition.formatShort(),
                style = PaletteTheme.styles.text.bodySmall,
            )
            Text(
                text = remember(timelineState) { (timelineState?.duration ?: Duration.ZERO).formatShort() },
                style = PaletteTheme.styles.text.bodySmall,
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
        )
    }
}
