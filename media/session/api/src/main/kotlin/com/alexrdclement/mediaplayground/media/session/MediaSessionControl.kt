package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl

interface MediaSessionControl {

    val mediaSessionState: MediaSessionState

    suspend fun getMediaEngineControl(): MediaEngineControl
}
