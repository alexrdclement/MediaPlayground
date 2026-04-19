package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.ImageId
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri
import kotlinx.io.files.Path

interface PathProvider {
    fun getAudioFilesDir(): Path
    fun getAudioFilePath(id: String, extension: String): Path
    fun getPath(uri: MediaAssetUri): Path
    fun getImagesDir(): Path
    fun getImagePath(imageId: ImageId, extension: String): Path
}
