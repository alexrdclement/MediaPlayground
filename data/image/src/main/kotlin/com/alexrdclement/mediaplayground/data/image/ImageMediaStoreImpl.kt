package com.alexrdclement.mediaplayground.data.image

import com.alexrdclement.mediaplayground.data.image.local.LocalImageDataStore
import com.alexrdclement.mediaplayground.media.model.Image
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.store.ImageMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class ImageMediaStoreImpl @Inject constructor(
    private val localImageDataStore: LocalImageDataStore,
) : ImageMediaStore {

    override fun getImageFlow(imageId: ImageId): Flow<Image?> =
        localImageDataStore.getImageFlow(imageId)

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(images: Set<Image>) =
        localImageDataStore.put(images)
}
