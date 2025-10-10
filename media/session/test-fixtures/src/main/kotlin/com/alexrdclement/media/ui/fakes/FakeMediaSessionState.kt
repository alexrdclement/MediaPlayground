package com.alexrdclement.media.ui.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeMediaEngineState
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import javax.inject.Inject

class FakeMediaSessionState @Inject constructor(
    override val mediaEngineState: MediaEngineState = FakeMediaEngineState(),
) : MediaSessionState
