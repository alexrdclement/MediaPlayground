package com.alexrdclement.mediaplayground.data.audioasset

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.audioasset.local.LocalAudioAssetDataStore
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class AudioAssetRepositoryImpl @Inject constructor(
    private val localAudioAssetDataStore: LocalAudioAssetDataStore,
) : AudioAssetRepository {

    override fun getAudioAssetFlow(id: AudioAssetId): Flow<AudioAsset?> =
        localAudioAssetDataStore.getAudioAssetFlow(id)

    override fun getAudioAssetPagingData(config: PagingConfig): Flow<PagingData<AudioAsset>> =
        localAudioAssetDataStore.getAudioAssetPagingData(config)

    override fun getAudioAssetCountFlow(): Flow<Int> =
        localAudioAssetDataStore.getAudioAssetCountFlow()

    override suspend fun put(audioAsset: AudioAsset) =
        localAudioAssetDataStore.put(audioAsset)

    override suspend fun delete(id: AudioAssetId) =
        localAudioAssetDataStore.delete(id)
}
