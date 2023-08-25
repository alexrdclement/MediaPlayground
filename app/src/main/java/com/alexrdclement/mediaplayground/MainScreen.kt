package com.alexrdclement.mediaplayground

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.alexrdclement.ui.components.MediaPickerButton
import com.alexrdclement.ui.components.MediaPlayer
import com.alexrdclement.ui.components.PlayerControls

@Composable
fun MainScreen(
    player: Player?,
    isPlaying: Boolean,
    onPickMediaClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
) {
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
        ) {
            if (player != null) {
                MediaPlayer(
                    player = player,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            ) {
                MediaPickerButton(onClick = onPickMediaClick)
                PlayerControls(
                    isPlaying = isPlaying,
                    onPlayPauseClicked = onPlayPauseClick,
                )
            }
        }
    }
}
