package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaybackRateControl
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakePlaybackRateControl : PlaybackRateControl {

    val mutablePlaybackRateState = MutableSharedFlow<PlaybackRateState>(replay = 1)

    override fun getPlaybackRateState(): Flow<PlaybackRateState> = mutablePlaybackRateState

    override suspend fun setSpeed(speed: Float) {
        mutablePlaybackRateState.emit(PlaybackRateState(speed = speed))
    }
}
