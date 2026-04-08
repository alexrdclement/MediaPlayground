package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.store.PathProvider
import kotlinx.io.files.Path

class FakePathProvider : PathProvider {
    override fun getAlbumDir(albumId: AlbumId): Path = Path("/tmp/albums/${albumId.value}")
    override fun getImagesDir(): Path = Path("/tmp/images")
    override fun getImagePath(imageId: ImageId, extension: String): Path =
        Path("/tmp/images/${imageId.value}.$extension")
}
