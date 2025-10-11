package com.alexrdclement.media.ui.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeMediaEngineState
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeMediaSessionState @Inject constructor(
    val fakeMediaEngineState: MediaEngineState = FakeMediaEngineState(),
) : MediaSessionState {

    val mutableMediaEngineState = MutableStateFlow(fakeMediaEngineState)
    override val mediaEngineState = mutableMediaEngineState
}
