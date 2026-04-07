package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.AlbumId
import com.alexrdclement.mediaplayground.media.model.ImageId
import kotlinx.io.files.Path

interface PathProvider {
    fun getAlbumDir(albumId: AlbumId): Path
    fun getImagesDir(): Path
    fun getImagePath(imageId: ImageId, extension: String): Path
}
