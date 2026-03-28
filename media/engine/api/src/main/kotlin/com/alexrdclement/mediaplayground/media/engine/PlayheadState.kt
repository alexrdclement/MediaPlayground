package com.alexrdclement.mediaplayground.media.engine

import kotlin.time.Duration
import kotlin.time.TimeMark

data class PlayheadState(
    val position: Duration,
    val capturedAt: TimeMark,
)
