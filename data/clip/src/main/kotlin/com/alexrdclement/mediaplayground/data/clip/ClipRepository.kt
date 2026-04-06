package com.alexrdclement.mediaplayground.data.clip

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import kotlinx.coroutines.flow.Flow

interface ClipRepository {
    suspend fun get(id: ClipId): Clip?
    fun getClipFlow(id: ClipId): Flow<Clip?>
    fun getClipPagingData(config: PagingConfig): Flow<PagingData<Clip>>
    fun getClipCountFlow(): Flow<Int>
    suspend fun put(clip: Clip)
    suspend fun update(id: ClipId, title: String)
    suspend fun delete(id: ClipId)
}
