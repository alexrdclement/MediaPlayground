package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.AudioClip
import com.alexrdclement.mediaplayground.media.model.ClipId
import com.alexrdclement.mediaplayground.media.model.MediaAssetId

interface ClipMediaStore {
    suspend fun get(id: ClipId): AudioClip?

    suspend fun getByMediaAssetId(id: MediaAssetId): AudioClip?

    context(scope: MediaStoreTransactionScope)
    suspend fun put(clip: AudioClip)
}
