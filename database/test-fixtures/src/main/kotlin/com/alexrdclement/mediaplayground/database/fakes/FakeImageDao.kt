package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.model.Image

class FakeImageDao : ImageDao {

    val images = mutableSetOf<Image>()

    override suspend fun getImage(id: String): Image? {
        return images.find { it.id == id }
    }

    override suspend fun getImagesForAlbum(albumId: String): List<Image> {
        return images.filter { it.albumId == albumId }
    }

    override suspend fun insert(vararg image: Image) {
        images.addAll(image)
    }

    override suspend fun delete(id: String) {
        images.removeIf { it.id == id }
    }
}
