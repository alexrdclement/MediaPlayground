package com.alexrdclement.mediaplayground.data.mediaasset

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import kotlinx.coroutines.flow.Flow

interface MediaAssetRepository {
    fun getMediaAssetFlow(id: MediaAssetId): Flow<MediaAsset?>
    fun getMediaAssetPagingData(config: PagingConfig): Flow<PagingData<MediaAsset>>
    fun getMediaAssetCountFlow(): Flow<Int>
    suspend fun put(mediaAsset: MediaAsset)
    suspend fun delete(id: MediaAssetId)
}
