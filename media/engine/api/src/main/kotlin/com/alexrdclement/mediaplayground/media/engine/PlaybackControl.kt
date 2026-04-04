package com.alexrdclement.mediaplayground.media.engine

import kotlinx.coroutines.flow.Flow

interface PlaybackControl {
    fun getPlaybackState(): Flow<PlaybackState>
    suspend fun setSpeed(speed: Float)
    suspend fun setPitch(pitch: Float)
}

suspend fun PlaybackControl.reset() {
    setSpeed(1f)
    setPitch(1f)
}
