package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.coroutines.flow.Flow

interface ImageMediaStore {
    fun getImageFlow(imageId: ImageId): Flow<Image?>

    context(scope: MediaStoreTransactionScope)
    suspend fun put(images: Set<Image>)
}
