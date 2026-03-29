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
import androidx.compose.ui.unit.dp
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.engine.isPlaying
import com.alexrdclement.palette.components.media.PlayPauseButton
import com.alexrdclement.palette.components.media.SkipBackButton
import com.alexrdclement.palette.components.media.SkipButton
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
fun TransportControlBar(
    transportState: TransportState,
    onSkipBackClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onSkipClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            PaletteTheme.spacing.medium,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        SkipBackButton(
            onClick = onSkipBackClick,
            modifier = Modifier
                .size(48.dp)
        )
        PlayPauseButton(
            isPlaying = transportState.isPlaying,
            onClick = onPlayPauseClick,
            modifier = Modifier
                .size(72.dp)
        )
        SkipButton(
            onClick = onSkipClick,
            modifier = Modifier
                .size(48.dp)
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
        )
    }
}
