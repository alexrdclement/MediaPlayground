package com.alexrdclement.mediaplayground.data.clip

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import kotlinx.coroutines.flow.Flow

interface ClipRepository {
    suspend fun get(id: ClipId): AudioClip?
    fun getClipFlow(id: ClipId): Flow<AudioClip?>
    fun getClipPagingData(config: PagingConfig): Flow<PagingData<AudioClip>>
    fun getClipCountFlow(): Flow<Int>
    suspend fun put(clip: AudioClip)
    suspend fun update(id: ClipId, title: String)
    suspend fun delete(id: ClipId)
}
