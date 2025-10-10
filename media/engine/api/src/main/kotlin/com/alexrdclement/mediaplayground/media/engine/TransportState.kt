package com.alexrdclement.mediaplayground.media.engine

enum class TransportState {
    Stopped,
    Paused,
    Playing,
    Buffering,
}

val TransportState.isPlaying: Boolean
    get() = this == TransportState.Playing
