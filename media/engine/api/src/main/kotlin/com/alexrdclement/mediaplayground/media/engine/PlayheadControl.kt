package com.alexrdclement.mediaplayground.media.engine

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface PlayheadControl {
    fun getPlayheadState(): Flow<PlayheadState>
    suspend fun getPlayheadPosition(): Duration
    suspend fun seek(position: Duration)
}
