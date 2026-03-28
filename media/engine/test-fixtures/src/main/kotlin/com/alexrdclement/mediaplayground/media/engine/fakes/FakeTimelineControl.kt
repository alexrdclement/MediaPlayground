package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.TimelineControl
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeTimelineControl : TimelineControl {

    val mutableTimelineState = MutableSharedFlow<TimelineState>(replay = 1)

    override fun getTimelineState(): Flow<TimelineState> = mutableTimelineState
}
