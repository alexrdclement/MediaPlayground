package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import com.alexrdclement.mediaplayground.media.store.PathProvider
import kotlinx.io.files.Path

class FakePathProvider : PathProvider {
    override fun getPath(uri: MediaAssetUri): Path = when (uri) {
        is MediaAssetUri.Shared -> Path("/tmp/images/${uri.fileName}")
        is MediaAssetUri.Album -> Path("/tmp/albums/${uri.albumId.value}/${uri.fileName}")
    }
}
