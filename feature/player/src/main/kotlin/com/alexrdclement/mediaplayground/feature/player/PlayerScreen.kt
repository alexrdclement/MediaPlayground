package com.alexrdclement.mediaplayground.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.ui.MediaPlayer
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.theme.PaletteTheme

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
        color = PaletteTheme.colorScheme.surface,
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
