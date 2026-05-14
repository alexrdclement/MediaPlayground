package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId

interface AudioAssetStore {
    suspend fun getByFileName(fileName: String): AudioAsset?

    context(scope: MediaStoreTransactionScope)
    suspend fun put(audioAsset: AudioAsset)

    context(scope: MediaStoreTransactionScope)
    suspend fun delete(id: AudioAssetId)
}
