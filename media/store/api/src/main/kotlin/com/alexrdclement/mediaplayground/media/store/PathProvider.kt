package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import kotlinx.io.files.Path

interface PathProvider {
    fun getPath(uri: MediaAssetUri): Path
}
