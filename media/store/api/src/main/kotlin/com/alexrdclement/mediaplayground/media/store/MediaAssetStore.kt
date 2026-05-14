package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAsset

interface MediaAssetStore {
    suspend fun put(asset: MediaAsset)
}
