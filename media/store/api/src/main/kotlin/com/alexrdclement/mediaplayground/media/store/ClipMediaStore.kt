package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.MediaAssetId

interface ClipMediaStore {
    suspend fun get(id: ClipId): Clip?

    suspend fun getByMediaAssetId(id: MediaAssetId): Clip?

    context(scope: MediaStoreTransactionScope)
    suspend fun put(clip: Clip)
}
