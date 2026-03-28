package com.alexrdclement.mediaplayground.media.engine

import kotlinx.coroutines.flow.Flow

interface TimelineControl {
    fun getTimelineState(): Flow<TimelineState>
}
