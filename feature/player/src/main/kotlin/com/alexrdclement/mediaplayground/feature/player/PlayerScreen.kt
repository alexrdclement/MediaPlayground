package com.alexrdclement.mediaplayground.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import dev.zacsweers.metrox.viewmodel.metroViewModel
import com.alexrdclement.mediaplayground.media.ui.MediaPlayer
import com.alexrdclement.palette.components.core.Surface
import com.alexrdclement.palette.theme.PaletteTheme

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = metroViewModel(),
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
