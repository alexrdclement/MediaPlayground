package com.alexrdclement.mediaplayground.data.disk.fakes

import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import kotlinx.io.files.Path

class FakePathProvider : PathProvider {
    override fun getPath(uri: MediaAssetUri): Path = when (uri) {
        is MediaAssetUri.Shared -> Path("file:/", "images", uri.fileName)
        is MediaAssetUri.Album -> Path("file:/", uri.albumId.value, uri.fileName)
    }
}
