package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl
import dev.zacsweers.metro.Inject

class MediaSessionControlImpl @Inject constructor(
    override val mediaSessionState: MediaSessionState,
    private val mediaEngineControl: MediaEngineControl,
) : MediaSessionControl {

    override suspend fun getMediaEngineControl(): MediaEngineControl {
        return mediaEngineControl
    }
}
