package com.alexrdclement.mediaplayground.data.disk.fakes

import com.alexrdclement.mediaplayground.data.disk.PathProvider
import kotlinx.io.files.Path

class FakePathProvider : PathProvider {
    override fun getAlbumDir(albumId: String): Path {
        // Path de-dupes slashes, even in the scheme
        return Path("file:/", albumId)
    }

    override fun getImagesDir(): Path {
        return Path("file:/", "images")
    }
}
