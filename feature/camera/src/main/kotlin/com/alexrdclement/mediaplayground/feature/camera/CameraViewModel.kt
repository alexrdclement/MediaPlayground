package com.alexrdclement.mediaplayground.feature.camera

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import dev.zacsweers.metro.Inject

class CameraViewModel @Inject constructor(
    val mediaSessionState: MediaSessionState,
) : ViewModel()
