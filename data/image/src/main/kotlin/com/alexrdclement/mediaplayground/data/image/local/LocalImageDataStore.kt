package com.alexrdclement.mediaplayground.data.image.local

import com.alexrdclement.mediaplayground.data.image.local.mapper.toImage
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalImageDataStore @Inject constructor(
    private val imageDao: ImageDao,
    private val pathProvider: PathProvider,
) {
    fun getImagesForAlbumFlow(albumId: AlbumId): Flow<List<Image>> {
        val albumDir = pathProvider.getAlbumDir(albumId.value)
        return imageDao.getImagesForAlbumFlow(albumId.value).map { images ->
            images.map { it.toImage(albumDir) }
        }
    }
}
