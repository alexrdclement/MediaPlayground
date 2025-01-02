package com.alexrdclement.mediaplayground.data.audio.local.fakes

import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import kotlinx.io.files.Path
import javax.inject.Inject

class FakePathProvider @Inject constructor(): PathProvider {
    private val importDir: Path = Path("file://")
    override fun getAlbumDir(albumId: String): Path {
        return Path(importDir, albumId)
    }
}
