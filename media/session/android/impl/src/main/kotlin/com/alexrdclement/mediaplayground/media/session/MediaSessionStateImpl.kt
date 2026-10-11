package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MediaSessionStateImpl @Inject constructor(
    localMediaEngineState: MediaEngineState,
) : MediaSessionState {
    override val mediaEngineState: Flow<MediaEngineState> = flowOf(localMediaEngineState)
}
