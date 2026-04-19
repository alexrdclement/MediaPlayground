package com.alexrdclement.mediaplayground.data.audioasset

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import kotlinx.coroutines.flow.Flow

interface AudioAssetRepository {
    fun getAudioAssetFlow(id: AudioAssetId): Flow<AudioAsset?>
    fun getAudioAssetPagingData(config: PagingConfig): Flow<PagingData<AudioAsset>>
    fun getAudioAssetCountFlow(): Flow<Int>
    suspend fun put(audioAsset: AudioAsset)
    suspend fun delete(id: AudioAssetId)
}
