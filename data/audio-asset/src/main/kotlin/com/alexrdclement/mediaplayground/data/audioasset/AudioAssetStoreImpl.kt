package com.alexrdclement.mediaplayground.data.audioasset

import com.alexrdclement.mediaplayground.data.audioasset.local.LocalAudioAssetDataStore
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.store.AudioAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class AudioAssetStoreImpl @Inject constructor(
    private val localAudioAssetDataStore: LocalAudioAssetDataStore,
) : AudioAssetStore {

    override suspend fun getByFileName(fileName: String): AudioAsset? =
        localAudioAssetDataStore.getByFileName(fileName)

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(audioAsset: AudioAsset) =
        localAudioAssetDataStore.put(audioAsset)

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(id: AudioAssetId) =
        localAudioAssetDataStore.delete(id)
}
