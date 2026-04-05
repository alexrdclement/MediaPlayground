package com.alexrdclement.mediaplayground.data.image.local

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.media.mediaimport.MediaImporter
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.io.files.Path

class LocalImageRepositoryImpl @Inject constructor(
    private val mediaImporter: MediaImporter,
    private val localImageDataStore: LocalImageDataStore,
    private val pathProvider: PathProvider,
) : LocalImageRepository {

    override fun getImageFlow(imageId: ImageId): Flow<Image?> =
        localImageDataStore.getImageFlow(imageId)

    override fun getImagePagingData(config: PagingConfig): Flow<PagingData<Image>> =
        localImageDataStore.getImagePagingData(config)

    override fun getImageCountFlow(): Flow<Int> =
        localImageDataStore.getImageCountFlow()

    override suspend fun updateImageNotes(id: ImageId, notes: String?) =
        localImageDataStore.updateImageNotes(id, notes)

    override suspend fun deleteImage(id: ImageId) =
        localImageDataStore.deleteImage(id)

    override suspend fun importImages(uris: List<Uri>) {
        val imagesDir = pathProvider.getImagesDir()
        mediaImporter.importImagesFromDisk(
            uris = uris,
            getDestination = { imageId, extension ->
                Path(imagesDir, "${imageId.value}.$extension")
            },
            saveImage = { imageId, fileName, metadata ->
                localImageDataStore.put(imageId = imageId, fileName = fileName, metadata = metadata)
            },
        )
    }
}
