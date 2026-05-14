package com.alexrdclement.mediaplayground.data.audioasset

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.audioasset.local.LocalAudioAssetStore
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class AudioAssetRepositoryImpl @Inject constructor(
    private val localAudioAssetStore: LocalAudioAssetStore,
) : AudioAssetRepository {

    override fun getFlow(id: AudioAssetId): Flow<AudioAsset?> =
        localAudioAssetStore.getFlow(id)

    override fun getPagingData(config: PagingConfig): Flow<PagingData<AudioAsset>> =
        localAudioAssetStore.getPagingData(config)

    override fun getCountFlow(): Flow<Int> =
        localAudioAssetStore.getCountFlow()

    override suspend fun put(audioAsset: AudioAsset) =
        localAudioAssetStore.put(audioAsset)

    override suspend fun delete(id: AudioAssetId) =
        localAudioAssetStore.delete(id)
}
