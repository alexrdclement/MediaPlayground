package com.alexrdclement.mediaplayground.data.image

import android.net.Uri
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImageFlow(imageId: ImageId): Flow<Image?>
    fun getImagePagingData(config: PagingConfig): Flow<PagingData<Image>>
    fun getImageCountFlow(): Flow<Int>
    suspend fun import(uris: List<Uri>)
    suspend fun put(images: Set<Image>)
    suspend fun updateImageNotes(id: ImageId, notes: String?)
    suspend fun delete(id: ImageId)
}
