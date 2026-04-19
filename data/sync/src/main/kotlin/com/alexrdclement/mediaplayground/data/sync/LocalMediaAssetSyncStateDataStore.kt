package com.alexrdclement.mediaplayground.data.sync

import com.alexrdclement.mediaplayground.database.dao.MediaAssetSyncStateDao
import com.alexrdclement.mediaplayground.database.model.MediaAssetSyncStateEntity
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Inject
class LocalMediaAssetSyncStateDataStore(
    private val dao: MediaAssetSyncStateDao,
) : MediaAssetSyncStateStore {
    fun getSyncStateFlow(id: MediaAssetId): Flow<MediaAssetSyncState?> =
        dao.getFlow(id.value).map { it?.syncState }

    override suspend fun upsert(id: MediaAssetId, syncState: MediaAssetSyncState) =
        dao.upsert(MediaAssetSyncStateEntity(mediaAssetId = id.value, syncState = syncState))

    override suspend fun delete(id: MediaAssetId) = dao.delete(id.value)
}
