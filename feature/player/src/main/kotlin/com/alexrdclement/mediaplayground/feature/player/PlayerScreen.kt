package com.alexrdclement.mediaplayground.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.ui.MediaPlayer
import com.alexrdclement.uiplayground.components.core.Surface
import com.alexrdclement.uiplayground.theme.PlaygroundTheme

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
) {
    val mediaSessionState = viewModel.mediaSessionState
    PlayerScreen(
        mediaSessionState = mediaSessionState,
    )
}

@Composable
fun PlayerScreen(
    mediaSessionState: MediaSessionState?,
) {
    Surface(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        color = PlaygroundTheme.colorScheme.surface,
    ) {
        if (mediaSessionState != null) {
            MediaPlayer(
                mediaSessionState = mediaSessionState,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}
