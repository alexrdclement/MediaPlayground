package com.alexrdclement.mediaplayground.data.image

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.image.Image
import com.alexrdclement.mediaplayground.media.model.image.ImageId
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImageFlow(imageId: ImageId): Flow<Image?>
    fun getImagePagingData(config: PagingConfig): Flow<PagingData<Image>>
    fun getImageCountFlow(): Flow<Int>
    suspend fun updateImageNotes(id: ImageId, notes: String?)
    suspend fun deleteImage(id: ImageId)
}
