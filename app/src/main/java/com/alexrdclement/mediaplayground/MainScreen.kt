package com.alexrdclement.mediaplayground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.alexrdclement.mediaplayground.ui.components.MediaPickerButton
import com.alexrdclement.mediaplayground.ui.components.PlayerControls

@Composable
fun MainScreen(
    isPlaying: Boolean,
    onPickMediaClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        MediaPickerButton(onClick = onPickMediaClick)
        PlayerControls(
            isPlaying = isPlaying,
            onPlayPauseClicked = onPlayPauseClick,
        )
    }
}
