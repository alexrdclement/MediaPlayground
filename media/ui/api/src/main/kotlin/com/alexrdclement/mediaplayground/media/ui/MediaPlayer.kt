package com.alexrdclement.mediaplayground.media.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexrdclement.mediaplayground.media.session.MediaSessionState

@Composable
fun MediaPlayer(
    mediaSessionState: MediaSessionState,
    modifier: Modifier = Modifier,
) {
    // TODO KMP: expect/actual
    AndroidMediaPlayer(
        mediaSessionState = mediaSessionState,
        modifier = modifier,
    )
}
