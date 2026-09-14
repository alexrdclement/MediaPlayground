package com.alexrdclement.mediaplayground.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.engine.isPlaying
import com.alexrdclement.mediaplayground.ui.theme.component.media.transportControlBar
import com.embarrasdf.palette.components.media.PlayPauseButton
import com.embarrasdf.palette.components.media.PlayPauseButtonStyle
import com.embarrasdf.palette.components.media.SkipBackButton
import com.embarrasdf.palette.components.media.SkipButton
import com.embarrasdf.palette.components.media.SkipButtonStyle
import com.embarrasdf.palette.theme.PaletteTheme

data class TransportControlBarStyle(
    val contentSpacing: Dp = 16.dp,
    val skipButtonSize: Dp = 48.dp,
    val playPauseButtonSize: Dp = 72.dp,
    val playPauseButtonStyle: PlayPauseButtonStyle = PlayPauseButtonStyle(),
    val skipButtonStyle: SkipButtonStyle = SkipButtonStyle(),
)

@Composable
fun TransportControlBar(
    transportState: TransportState,
    onSkipBackClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: TransportControlBarStyle = TransportControlBarStyle(),
    onPlayPauseLongClick: (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            style.contentSpacing,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        SkipBackButton(
            onClick = onSkipBackClick,
            modifier = Modifier
                .size(style.skipButtonSize),
            style = style.skipButtonStyle,
        )
        PlayPauseButton(
            isPlaying = transportState.isPlaying,
            onClick = onPlayPauseClick,
            onLongClick = onPlayPauseLongClick,
            modifier = Modifier
                .size(style.playPauseButtonSize),
            style = style.playPauseButtonStyle,
        )
        SkipButton(
            onClick = onSkipClick,
            modifier = Modifier
                .size(style.skipButtonSize),
            style = style.skipButtonStyle,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    PaletteTheme {
        TransportControlBar(
            transportState = TransportState.Playing,
            onSkipBackClick = {},
            onPlayPauseClick = {},
            onSkipClick = {},
            style = PaletteTheme.component.media.transportControlBar,
        )
    }
}
