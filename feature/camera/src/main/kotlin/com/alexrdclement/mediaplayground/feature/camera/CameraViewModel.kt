package com.alexrdclement.mediaplayground.feature.camera

import androidx.lifecycle.ViewModel
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    val mediaSessionState: MediaSessionState,
) : ViewModel()
