package com.alexrdclement.mediaplayground.data.disk.fakes

import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.io.files.Path

class FakePathProvider : PathProvider {
    override fun getAlbumDir(albumId: AlbumId): Path {
        return Path("file:/", albumId.value)
    }

    override fun getImagesDir(): Path {
        return Path("file:/", "images")
    }

    override fun getImagePath(imageId: ImageId, extension: String): Path {
        return Path(getImagesDir(), "${imageId.value}.$extension")
    }
}
