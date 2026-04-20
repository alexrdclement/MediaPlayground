package com.alexrdclement.mediaplayground.data.audioasset

import com.alexrdclement.mediaplayground.data.audioasset.local.LocalAudioAssetStore
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.store.AudioAssetStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class AudioAssetStoreImpl @Inject constructor(
    private val localAudioAssetStore: LocalAudioAssetStore,
) : AudioAssetStore {

    override suspend fun getByFileName(fileName: String): AudioAsset? =
        localAudioAssetStore.getByFileName(fileName)

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(audioAsset: AudioAsset) =
        localAudioAssetStore.put(audioAsset)

    context(scope: MediaStoreTransactionScope)
    override suspend fun delete(id: AudioAssetId) =
        localAudioAssetStore.delete(id)
}
