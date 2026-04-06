package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId

interface MediaAssetStore {
    context(scope: MediaStoreTransactionScope)
    suspend fun put(mediaAsset: MediaAsset)

    context(scope: MediaStoreTransactionScope)
    suspend fun delete(id: MediaAssetId)
}
