package com.alexrdclement.mediaplayground.data.audio.local.fakes

import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import kotlinx.io.files.Path

class FakePathProvider : PathProvider {
    override fun getAlbumDir(albumId: String): Path {
        // Path de-dupes slashes, even in the scheme
        return Path("file:/", albumId)
    }
}
