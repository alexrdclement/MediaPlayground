package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.store.AudioAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

class FakeAudioAssetStore : AudioAssetStore {

    private val assets = mutableMapOf<AudioAssetId, AudioAsset>()

    override suspend fun getByFileName(fileName: String): AudioAsset? =
        assets.values.find { it.uri.toUriString().substringAfterLast('/') == fileName }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(audioAsset: AudioAsset) {
        assets[audioAsset.id] = audioAsset
    }

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(id: AudioAssetId) {
        assets.remove(id)
    }
}
