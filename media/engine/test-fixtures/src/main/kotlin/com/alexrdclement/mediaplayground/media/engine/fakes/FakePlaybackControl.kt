package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaybackControl
import com.alexrdclement.mediaplayground.media.engine.PlaybackState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakePlaybackControl : PlaybackControl {

    val mutablePlaybackState = MutableSharedFlow<PlaybackState>(replay = 1)

    override fun getPlaybackState(): Flow<PlaybackState> = mutablePlaybackState

    override suspend fun setSpeed(speed: Float) {
        val current = mutablePlaybackState.replayCache.firstOrNull() ?: PlaybackState()
        mutablePlaybackState.emit(current.copy(speed = speed))
    }

    override suspend fun setPitch(pitch: Float) {
        val current = mutablePlaybackState.replayCache.firstOrNull() ?: PlaybackState()
        mutablePlaybackState.emit(current.copy(pitch = pitch))
    }
}
