package com.alexrdclement.mediaplayground.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.ui.MediaPlayer
import com.embarrasdf.palette.components.core.Surface
import com.embarrasdf.palette.theme.PaletteTheme
import dev.zacsweers.metrox.viewmodel.metroViewModel

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
        style = PaletteTheme.component.core.surface.default,
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
