package com.alexrdclement.mediaplayground.data.clip

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.clip.local.LocalClipDataStore
import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class ClipRepositoryImpl @Inject constructor(
    private val localClipDataStore: LocalClipDataStore,
) : ClipRepository {

    override suspend fun get(id: ClipId): AudioClip? =
        localClipDataStore.get(id = id)

    override fun getClipFlow(id: ClipId): Flow<AudioClip?> =
        localClipDataStore.getClipFlow(id)

    override fun getClipPagingData(config: PagingConfig): Flow<PagingData<AudioClip>> =
        localClipDataStore.getClipPagingData(config)

    override fun getClipCountFlow(): Flow<Int> =
        localClipDataStore.getClipCountFlow()

    override suspend fun put(clip: AudioClip) =
        localClipDataStore.put(clip)

    override suspend fun update(id: ClipId, title: String) =
        localClipDataStore.updateClipTitle(id, title)

    override suspend fun delete(id: ClipId) =
        localClipDataStore.delete(id)
}
