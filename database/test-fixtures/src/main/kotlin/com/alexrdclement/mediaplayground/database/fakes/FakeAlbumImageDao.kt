package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AlbumImageDao
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.ImageFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class FakeAlbumImageDao(
    private val imageDao: FakeImageFileDao = FakeImageFileDao(),
) : AlbumImageDao {

    val albumImages = mutableSetOf<AlbumImageCrossRef>()
    private val albumImagesFlow = MutableStateFlow(emptySet<AlbumImageCrossRef>())

    override suspend fun insert(vararg albumImage: AlbumImageCrossRef) {
        albumImages.addAll(albumImage)
        albumImagesFlow.value = albumImagesFlow.value + albumImage.toSet()
    }

    override suspend fun delete(albumImage: AlbumImageCrossRef) {
        albumImages.remove(albumImage)
        albumImagesFlow.value = albumImagesFlow.value - albumImage
    }

    override suspend fun deleteForAlbum(albumId: String) {
        albumImages.removeAll { it.albumId == albumId }
        albumImagesFlow.value = albumImagesFlow.value.filterNot { it.albumId == albumId }.toSet()
    }

    override fun getImagesForAlbumFlow(albumId: String): Flow<List<ImageFile>> {
        return combine(albumImagesFlow, imageDao.images) { albumImages, images ->
            val imageIds = albumImages.filter { it.albumId == albumId }.map { it.imageId }.toSet()
            images.filter { it.id in imageIds }
        }
    }
}
