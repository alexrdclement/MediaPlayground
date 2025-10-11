package com.alexrdclement.mediaplayground.media.engine

interface MediaEngineControl {
    val mediaEngineState: MediaEngineState
    val transportControl: TransportControl
    val playlistControl: PlaylistControl
}
