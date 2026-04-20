package com.alexrdclement.mediaplayground.data.mediaasset

import com.alexrdclement.mediaplayground.data.audioasset.local.LocalAudioAssetStore
import com.alexrdclement.mediaplayground.data.image.local.LocalImageDataStore
import com.alexrdclement.mediaplayground.media.model.AudioAssetId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaAssetId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class MediaAssetRepositoryImpl @Inject constructor(
    private val localAudioAssetStore: LocalAudioAssetStore,
    private val localImageDataStore: LocalImageDataStore,
) : MediaAssetRepository {

    override fun getMediaAssetFlow(id: MediaAssetId): Flow<MediaAsset?> = when (id) {
        is AudioAssetId -> localAudioAssetStore.getFlow(id)
        is ImageId -> localImageDataStore.getImageFlow(id)
    }

    override suspend fun delete(id: MediaAssetId) = when (id) {
        is AudioAssetId -> localAudioAssetStore.delete(id)
        is ImageId -> localImageDataStore.delete(id)
    }
}
