package com.alexrdclement.mediaplayground.feature.player

import androidx.lifecycle.ViewModel
import androidx.media3.common.Player
import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    mediaSessionManager: MediaSessionManager,
) : ViewModel() {
    val player: StateFlow<Player?> = mediaSessionManager.player
}
