package com.alexrdclement.mediaplayground.data.audioasset

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.AudioAsset
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import kotlinx.coroutines.flow.Flow

interface AudioAssetRepository {
    fun getFlow(id: AudioAssetId): Flow<AudioAsset?>
    fun getPagingData(config: PagingConfig): Flow<PagingData<AudioAsset>>
    fun getCountFlow(): Flow<Int>
    suspend fun put(audioAsset: AudioAsset)
    suspend fun delete(id: AudioAssetId)
}
