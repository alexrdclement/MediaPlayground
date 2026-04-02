package com.alexrdclement.mediaplayground.data.image.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.image.local.mapper.toImage
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.media.model.image.Image
import com.alexrdclement.mediaplayground.media.model.image.ImageId
import com.alexrdclement.mediaplayground.media.model.image.ImageMetadata
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.alexrdclement.mediaplayground.database.model.Image as ImageEntity

class LocalImageDataStore @Inject constructor(
    private val imageDao: ImageDao,
    private val pathProvider: PathProvider,
) {
    fun getImageFlow(imageId: ImageId): Flow<Image?> {
        val imagesDir = pathProvider.getImagesDir()
        return imageDao.getImageFlow(imageId.value).map { it?.toImage(imagesDir) }
    }

    fun getImagePagingData(config: PagingConfig): Flow<PagingData<Image>> {
        val imagesDir = pathProvider.getImagesDir()
        return Pager(config = config) {
            imageDao.getImagesPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toImage(imagesDir) }
        }
    }

    fun getImageCountFlow(): Flow<Int> = imageDao.getImageCountFlow()

    suspend fun put(
        imageId: ImageId,
        fileName: String,
        metadata: ImageMetadata? = null,
        notes: String? = null,
    ) {
        imageDao.insert(
            ImageEntity(
                id = imageId.value,
                fileName = fileName,
                widthPx = metadata?.widthPx,
                heightPx = metadata?.heightPx,
                dateTimeOriginal = metadata?.dateTimeOriginal,
                gpsLatitude = metadata?.gpsLatitude,
                gpsLongitude = metadata?.gpsLongitude,
                cameraMake = metadata?.cameraMake,
                cameraModel = metadata?.cameraModel,
                notes = notes,
            )
        )
    }

    suspend fun updateImageNotes(imageId: ImageId, notes: String?) {
        val image = imageDao.getImage(imageId.value) ?: return
        imageDao.update(image.copy(notes = notes))
    }

    suspend fun deleteImage(imageId: ImageId) {
        imageDao.delete(imageId.value)
    }
}
