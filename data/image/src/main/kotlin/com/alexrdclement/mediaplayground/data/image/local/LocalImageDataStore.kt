package com.alexrdclement.mediaplayground.data.image.local

import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.image.local.mapper.toImage
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalImageDataStore @Inject constructor(
    private val imageDao: ImageDao,
    private val pathProvider: PathProvider,
) {
    fun getImageFlow(imageId: ImageId): Flow<Image?> {
        val imagesDir = pathProvider.getImagesDir()
        return imageDao.getImageFlow(imageId.value).map { it?.toImage(imagesDir) }
    }
}
