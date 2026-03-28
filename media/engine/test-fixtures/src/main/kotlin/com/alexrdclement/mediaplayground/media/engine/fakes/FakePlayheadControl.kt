package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlayheadControl
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import kotlin.time.Duration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakePlayheadControl : PlayheadControl {

    val mutablePlayheadState = MutableSharedFlow<PlayheadState>(replay = 1)
    var seekPosition: Duration? = null

    override fun getPlayheadState(): Flow<PlayheadState> = mutablePlayheadState

    override suspend fun seek(position: Duration) {
        seekPosition = position
    }
}
