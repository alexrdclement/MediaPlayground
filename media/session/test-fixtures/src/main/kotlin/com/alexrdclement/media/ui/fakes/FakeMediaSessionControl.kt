package com.alexrdclement.media.ui.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeMediaEngineControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import javax.inject.Inject

class FakeMediaSessionControl @Inject constructor(
    override val mediaSessionState: MediaSessionState = FakeMediaSessionState(),
    private val mediaEngineControl: MediaEngineControl = FakeMediaEngineControl(),
) : MediaSessionControl {

    override suspend fun getMediaEngineControl(): MediaEngineControl {
        return mediaEngineControl
    }
}
