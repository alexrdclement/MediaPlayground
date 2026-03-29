package com.alexrdclement.mediaplayground.data.image.local

import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class LocalImageRepositoryImpl @Inject constructor(
    private val localImageDataStore: LocalImageDataStore,
) : LocalImageRepository {

    override fun getImageFlow(imageId: ImageId): Flow<Image?> =
        localImageDataStore.getImageFlow(imageId)
}
