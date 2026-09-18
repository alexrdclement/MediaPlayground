package com.alexrdclement.mediaplayground.feature.player

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.ui.MediaPlayer
import com.embarrasdf.palette.theme.components.core.Surface
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
