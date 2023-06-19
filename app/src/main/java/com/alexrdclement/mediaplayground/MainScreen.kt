package com.alexrdclement.mediaplayground

import androidx.compose.runtime.Composable
import com.alexrdclement.mediaplayground.ui.components.PlayerControls

@Composable
fun MainScreen(
    isPlaying: Boolean,
    onPlayPauseClicked: () -> Unit,
) {
    PlayerControls(
        isPlaying = isPlaying,
        onPlayPauseClicked = onPlayPauseClicked,
    )
}
