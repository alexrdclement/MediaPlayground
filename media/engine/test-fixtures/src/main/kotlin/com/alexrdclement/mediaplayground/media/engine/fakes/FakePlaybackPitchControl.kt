package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaybackPitchControl
import com.alexrdclement.mediaplayground.media.engine.PlaybackPitchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakePlaybackPitchControl : PlaybackPitchControl {

    val mutablePlaybackPitchState = MutableSharedFlow<PlaybackPitchState>(replay = 1)

    override fun getPlaybackPitchState(): Flow<PlaybackPitchState> = mutablePlaybackPitchState

    override suspend fun setPitch(pitch: Float) {
        mutablePlaybackPitchState.emit(PlaybackPitchState(pitch = pitch))
    }
}
