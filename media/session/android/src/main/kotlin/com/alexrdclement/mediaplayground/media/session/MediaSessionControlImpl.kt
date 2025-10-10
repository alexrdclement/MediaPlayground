package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaSessionControlImpl @Inject constructor(
    override val mediaSessionState: MediaSessionState,
    private val mediaEngineControl: MediaEngineControl,
) : MediaSessionControl {

    override suspend fun getMediaEngineControl(): MediaEngineControl {
        return mediaEngineControl
    }
}
