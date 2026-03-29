package com.alexrdclement.mediaplayground.data.image

import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    fun getImageFlow(imageId: ImageId): Flow<Image?>
}
