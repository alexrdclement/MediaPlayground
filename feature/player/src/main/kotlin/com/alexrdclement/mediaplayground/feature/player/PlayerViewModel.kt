package com.alexrdclement.mediaplayground.feature.player

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import dev.zacsweers.metro.Inject

class PlayerViewModel @Inject constructor(
    val mediaSessionState: MediaSessionState,
) : ViewModel()
