package com.alexrdclement.mediaplayground.data.mediaasset

import com.alexrdclement.mediaplayground.data.mediaasset.local.LocalMediaAssetDataStore
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class MediaAssetStoreImpl @Inject constructor(
    private val localMediaAssetDataStore: LocalMediaAssetDataStore,
) : MediaAssetStore {

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(mediaAsset: MediaAsset) {
        localMediaAssetDataStore.put(mediaAsset)
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(id: MediaAssetId) {
        localMediaAssetDataStore.delete(id)
    }
}
