package com.alexrdclement.mediaplayground.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player
import com.alexrdclement.ui.components.MediaPlayer

@Composable
fun PlayerScreen(
    player: Player?,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        if (player != null) {
            MediaPlayer(
                player = player,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}
