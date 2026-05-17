package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.store.MediaAssetStore

class FakeMediaAssetStore : MediaAssetStore {
    override suspend fun put(asset: MediaAsset) = Unit
}
