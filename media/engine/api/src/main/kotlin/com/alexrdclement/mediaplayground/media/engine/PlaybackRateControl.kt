package com.alexrdclement.mediaplayground.media.engine

import kotlinx.coroutines.flow.Flow

interface PlaybackRateControl {
    fun getPlaybackRateState(): Flow<PlaybackRateState>
    suspend fun setSpeed(speed: Float)
}

suspend fun PlaybackRateControl.resetSpeed() {
    setSpeed(1f)
}
