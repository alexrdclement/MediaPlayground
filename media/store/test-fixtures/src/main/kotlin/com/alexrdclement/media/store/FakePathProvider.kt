package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.store.PathProvider
import kotlinx.io.files.Path

class FakePathProvider : PathProvider {
    override fun getAudioFilesDir(): Path = Path("/tmp/audio")
    override fun getAudioFilePath(id: String, extension: String): Path = Path("/tmp/audio/$id.$extension")
    override fun getPath(uri: MediaAssetUri): Path = when (uri) {
        is MediaAssetUri.Shared -> Path("/tmp/shared/${uri.fileName}")
        is MediaAssetUri.Album -> Path("/tmp/albums/${uri.albumId.value}/${uri.fileName}")
    }
    override fun getImagesDir(): Path = Path("/tmp/images")
    override fun getImagePath(imageId: ImageId, extension: String): Path =
        Path("/tmp/images/${imageId.value}.$extension")
}
