package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.store.ImageMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeImageMediaStore : ImageMediaStore {

    private val imagesFlow = MutableStateFlow<Map<ImageId, Image>>(emptyMap())

    override fun getImageFlow(imageId: ImageId): Flow<Image?> =
        imagesFlow.map { it[imageId] }

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(images: Set<Image>) {
        imagesFlow.update { current -> current + images.associateBy { it.id } }
    }
}
