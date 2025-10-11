package com.alexrdclement.mediaplayground.media.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.alexrdclement.mediaplayground.media.engine.AndroidMediaEngineState
import com.alexrdclement.mediaplayground.media.engine.getPlayer
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.ui.util.rememberLifecycleEvent
import kotlinx.coroutines.flow.map

@Composable
fun AndroidMediaPlayer(
    mediaSessionState: MediaSessionState,
    modifier: Modifier = Modifier,
) {
    val player by mediaSessionState.mediaEngineState
        .map { (it as? AndroidMediaEngineState)?.getPlayer() }
        .collectAsStateWithLifecycle(null)
    player?.let { player ->
        MediaPlayer(
            player = player,
            modifier = modifier,
        )
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun MediaPlayer(
    player: Player,
    modifier: Modifier = Modifier,
) {
    val lifecycleEvent = rememberLifecycleEvent()

    // PlayerView wouldn't directly respect size constraints
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier,
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                }
            },
            update = { playerView ->
                when (lifecycleEvent) {
                    Lifecycle.Event.ON_CREATE -> {}
                    Lifecycle.Event.ON_START -> {}
                    Lifecycle.Event.ON_RESUME -> {
                        playerView.onResume()
                    }
                    Lifecycle.Event.ON_PAUSE -> {
                        playerView.onPause()
                    }
                    Lifecycle.Event.ON_STOP -> {}
                    Lifecycle.Event.ON_DESTROY -> {}
                    Lifecycle.Event.ON_ANY -> {}
                }
            },
        )
    }
}
