package com.alexrdclement.mediaplayground.media.engine

import kotlin.time.Duration
import kotlin.time.TimeMark

data class PlayheadState(
    val positionSnapshot: Duration,
    val capturedAt: TimeMark,
)
