package com.alexrdclement.mediaplayground.data.mediaasset

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.mediaasset.local.LocalMediaAssetDataStore
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class MediaAssetRepositoryImpl @Inject constructor(
    private val localMediaAssetDataStore: LocalMediaAssetDataStore,
) : MediaAssetRepository {

    override fun getMediaAssetFlow(id: MediaAssetId): Flow<MediaAsset?> =
        localMediaAssetDataStore.getMediaAssetFlow(id)

    override fun getMediaAssetPagingData(config: PagingConfig): Flow<PagingData<MediaAsset>> =
        localMediaAssetDataStore.getMediaAssetPagingData(config)

    override fun getMediaAssetCountFlow(): Flow<Int> =
        localMediaAssetDataStore.getMediaAssetCountFlow()

    override suspend fun put(mediaAsset: MediaAsset) =
        localMediaAssetDataStore.put(mediaAsset)

    override suspend fun delete(id: MediaAssetId) =
        localMediaAssetDataStore.delete(id)
}
