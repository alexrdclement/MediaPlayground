package com.alexrdclement.mediaplayground.data.disk

import kotlinx.io.files.Path

interface PathProvider {
    fun getAlbumDir(albumId: String): Path
}
