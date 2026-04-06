package com.alexrdclement.mediaplayground.data.image.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.database.dao.ImageFileDao
import com.alexrdclement.mediaplayground.database.mapping.toImage
import com.alexrdclement.mediaplayground.database.mapping.toImageEntity
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteImage
import com.alexrdclement.mediaplayground.database.transaction.insertImageFile
import com.alexrdclement.mediaplayground.database.transaction.insertImageFiles
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.alexrdclement.mediaplayground.database.model.ImageFile as ImageEntity

class LocalImageDataStore @Inject constructor(
    private val imageFileDao: ImageFileDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    private val pathProvider: PathProvider,
) {
    fun getImageFlow(imageId: ImageId): Flow<Image?> {
        val imagesDir = pathProvider.getImagesDir()
        return imageFileDao.getImageFlow(imageId.value).map { it?.toImage(imagesDir) }
    }

    fun getImagePagingData(config: PagingConfig): Flow<PagingData<Image>> {
        val imagesDir = pathProvider.getImagesDir()
        return Pager(config = config) {
            imageFileDao.getImagesPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toImage(imagesDir) }
        }
    }

    fun getImageCountFlow(): Flow<Int> = imageFileDao.getImageCountFlow()

    suspend fun put(
        imageId: ImageId,
        fileName: String,
        mediaMetadata: MediaMetadata.Image,
        notes: String? = null,
    ) = databaseTransactionRunner.run {
        insertImageFile(
            ImageEntity(
                id = imageId.value,
                fileName = fileName,
                mimeType = mediaMetadata.mimeType,
                extension = mediaMetadata.extension,
                widthPx = mediaMetadata.widthPx,
                heightPx = mediaMetadata.heightPx,
                dateTimeOriginal = mediaMetadata.dateTimeOriginal,
                gpsLatitude = mediaMetadata.gpsLatitude,
                gpsLongitude = mediaMetadata.gpsLongitude,
                cameraMake = mediaMetadata.cameraMake,
                cameraModel = mediaMetadata.cameraModel,
                notes = notes,
            )
        )
    }

    suspend fun put(images: Set<Image>) = databaseTransactionRunner.run {
        insertImageFiles(*images.map { it.toImageEntity() }.toTypedArray())
    }

    suspend fun updateImageNotes(imageId: ImageId, notes: String?) = databaseTransactionRunner.run {
        val image = imageFileDao.getImage(imageId.value) ?: return@run
        imageFileDao.update(image.copy(notes = notes))
    }

    suspend fun delete(imageId: ImageId) = databaseTransactionRunner.run {
        deleteImage(imageId.value)
    }
}
