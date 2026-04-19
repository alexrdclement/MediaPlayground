package com.alexrdclement.mediaplayground.data.image.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexrdclement.mediaplayground.database.dao.ImageAssetDao
import com.alexrdclement.mediaplayground.database.mapping.toImage
import com.alexrdclement.mediaplayground.database.mapping.toImageEntity
import com.alexrdclement.mediaplayground.database.mapping.toMediaAssetRecord
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.deleteImage
import com.alexrdclement.mediaplayground.database.transaction.insertImageAssets
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalImageDataStore @Inject constructor(
    private val imageAssetDao: ImageAssetDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
) {
    fun getImageFlow(imageId: ImageId): Flow<Image?> {
        return imageAssetDao.getImageFlow(imageId.value).map { it?.toImage() }
    }

    fun getImagePagingData(config: PagingConfig): Flow<PagingData<Image>> {
        return Pager(config = config) {
            imageAssetDao.getImagesPagingSource()
        }.flow.map { pagingData ->
            pagingData.map { it.toImage() }
        }
    }

    fun getImageCountFlow(): Flow<Int> = imageAssetDao.getImageCountFlow()

    suspend fun put(images: Set<Image>) = databaseTransactionRunner.run {
        insertImageAssets(*images.map { it.toMediaAssetRecord() to it.toImageEntity() }.toTypedArray())
    }

    suspend fun updateImageNotes(imageId: ImageId, notes: String?) = databaseTransactionRunner.run {
        val image = imageAssetDao.getImage(imageId.value) ?: return@run
        imageAssetDao.update(image.imageAsset.copy(notes = notes))
    }

    suspend fun delete(imageId: ImageId) = databaseTransactionRunner.run {
        deleteImage(imageId.value)
    }
}
