package com.alexrdclement.mediaplayground.media.engine

import kotlin.time.Duration
import kotlinx.coroutines.flow.Flow

interface PlayheadControl {
    fun getPlayheadState(): Flow<PlayheadState>
    suspend fun seek(position: Duration)
}
