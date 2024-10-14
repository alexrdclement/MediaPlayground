package com.alexrdclement.mediaplayground.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.Player
import com.alexrdclement.mediaplayground.ui.components.MediaPlayer
import com.alexrdclement.uiplayground.components.Surface
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

@Composable
fun PlayerScreen(
    player: Player?,
) {
    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        color = PlaygroundTheme.colorScheme.surface,
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
