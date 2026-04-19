package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState
import com.alexrdclement.mediaplayground.media.store.MediaAssetSyncStateStore

class FakeMediaAssetSyncStateStore : MediaAssetSyncStateStore {
    val states = mutableMapOf<MediaAssetId, MediaAssetSyncState>()

    override suspend fun upsert(id: MediaAssetId, syncState: MediaAssetSyncState) {
        states[id] = syncState
    }

    override suspend fun delete(id: MediaAssetId) {
        states.remove(id)
    }
}
