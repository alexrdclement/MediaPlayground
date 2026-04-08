package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

class FakeMediaAssetStore : MediaAssetStore {

    private val assets = mutableMapOf<MediaAssetId, MediaAsset>()

    override suspend fun getByFileName(fileName: String): MediaAsset? =
        assets.values.find { it.uri?.substringAfterLast('/') == fileName }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(mediaAsset: MediaAsset) {
        assets[mediaAsset.id] = mediaAsset
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(id: MediaAssetId) {
        assets.remove(id)
    }
}
