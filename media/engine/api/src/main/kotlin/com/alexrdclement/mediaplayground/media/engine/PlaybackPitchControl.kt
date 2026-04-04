package com.alexrdclement.mediaplayground.media.engine

import kotlinx.coroutines.flow.Flow

interface PlaybackPitchControl {
    fun getPlaybackPitchState(): Flow<PlaybackPitchState>
    suspend fun setPitch(pitch: Float)
}

suspend fun PlaybackPitchControl.resetPitch() {
    setPitch(1f)
}
