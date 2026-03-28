package com.alexrdclement.mediaplayground.media.engine

import dev.zacsweers.metro.Inject

class AndroidMediaEngineControl @Inject constructor(
    override val mediaEngineState: AndroidMediaEngineState,
    override val transportControl: TransportControlImpl,
    override val playlistControl: PlaylistControl,
) : MediaEngineControl
