package com.alexrdclement.mediaplayground.data.image

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.image.local.LocalImageDataStore
import com.alexrdclement.mediaplayground.media.mediaimport.ImageImporter
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class ImageRepositoryImpl @Inject constructor(
    private val localImageDataStore: LocalImageDataStore,
    private val mediaImporter: ImageImporter,
) : ImageRepository {

    override fun getImageFlow(imageId: ImageId): Flow<Image?> =
        localImageDataStore.getImageFlow(imageId)

    override fun getImagePagingData(config: PagingConfig): Flow<PagingData<Image>> =
        localImageDataStore.getImagePagingData(config)

    override fun getImageCountFlow(): Flow<Int> =
        localImageDataStore.getImageCountFlow()

    override suspend fun import(uris: List<Uri>) {
        mediaImporter.import(uris = uris)
    }

    override suspend fun put(images: Set<Image>) {
        localImageDataStore.put(images = images)
    }

    override suspend fun updateImageNotes(id: ImageId, notes: String?) =
        localImageDataStore.updateImageNotes(id, notes)

    override suspend fun delete(id: ImageId) =
        localImageDataStore.delete(id)
}
