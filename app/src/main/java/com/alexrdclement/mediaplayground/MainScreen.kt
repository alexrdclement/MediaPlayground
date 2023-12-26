package com.alexrdclement.mediaplayground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexrdclement.ui.components.MediaPickerButton
import com.alexrdclement.uiplayground.components.PlayPauseButton

@Composable
fun MainScreen(
    isPlaying: Boolean,
    onPickMediaClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                MediaPickerButton(onClick = onPickMediaClick)
                PlayPauseButton(
                    isPlaying = isPlaying,
                    onClick = onPlayPauseClick,
                )
            }
        }
    }
}
