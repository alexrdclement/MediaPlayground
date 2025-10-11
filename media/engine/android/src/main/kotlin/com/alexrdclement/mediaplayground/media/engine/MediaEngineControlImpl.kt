package com.alexrdclement.mediaplayground.media.engine

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaEngineControlImpl @Inject constructor(
    override val mediaEngineState: MediaEngineStateImpl,
    override val transportControl: TransportControlImpl,
    override val playlistControl: PlaylistControl,
) : MediaEngineControl
