package com.alexrdclement.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    onPlayPauseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onPlayPauseClicked,
        modifier = modifier,
    ) {
        Text(text = if (isPlaying) "Pause" else "Play")
    }
}
