package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState

interface MediaAssetSyncStateStore {
    suspend fun upsert(id: MediaAssetId, syncState: MediaAssetSyncState)
    suspend fun delete(id: MediaAssetId)
}
