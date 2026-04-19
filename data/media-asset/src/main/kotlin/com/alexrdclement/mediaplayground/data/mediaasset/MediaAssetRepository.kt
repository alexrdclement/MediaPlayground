package com.alexrdclement.mediaplayground.data.mediaasset

import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import kotlinx.coroutines.flow.Flow

interface MediaAssetRepository {
    fun getMediaAssetFlow(id: MediaAssetId): Flow<MediaAsset?>
    suspend fun delete(id: MediaAssetId)
}
