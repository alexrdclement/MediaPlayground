package com.alexrdclement.mediaplayground.media.session

import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import javax.inject.Inject

class MediaSessionStateImpl @Inject constructor(
    override val mediaEngineState: MediaEngineState,
) : MediaSessionState
