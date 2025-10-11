package com.alexrdclement.mediaplayground.media.engine

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidMediaEngineControl @Inject constructor(
    override val mediaEngineState: AndroidMediaEngineState,
    override val transportControl: TransportControlImpl,
    override val playlistControl: PlaylistControl,
) : MediaEngineControl
