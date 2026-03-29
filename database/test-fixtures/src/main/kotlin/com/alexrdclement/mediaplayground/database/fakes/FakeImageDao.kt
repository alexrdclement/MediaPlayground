package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.model.Image
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeImageDao : ImageDao {

    val images = MutableStateFlow(emptySet<Image>())

    override suspend fun getImage(id: String): Image? {
        return images.value.find { it.id == id }
    }

    override suspend fun getImagesForAlbum(albumId: String): List<Image> {
        return images.value.filter { it.albumId == albumId }
    }

    override fun getImagesForAlbumFlow(albumId: String): Flow<List<Image>> {
        return images.map { it.filter { image -> image.albumId == albumId } }
    }

    override suspend fun insert(vararg image: Image) {
        images.value = images.value + image.toSet()
    }

    override suspend fun delete(id: String) {
        images.value = images.value.filterNot { it.id == id }.toSet()
    }

    override suspend fun deleteImagesForAlbum(albumId: String) {
        images.value = images.value.filterNot { it.albumId == albumId }.toSet()
    }
}
