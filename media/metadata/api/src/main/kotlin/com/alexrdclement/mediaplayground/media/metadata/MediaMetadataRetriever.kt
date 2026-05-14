package com.alexrdclement.mediaplayground.media.metadata

import android.net.Uri
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import kotlinx.io.files.Path

interface MediaMetadataRetriever {
    suspend fun getMediaMetadata(filePath: Path): MediaMetadata
    suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata
}
