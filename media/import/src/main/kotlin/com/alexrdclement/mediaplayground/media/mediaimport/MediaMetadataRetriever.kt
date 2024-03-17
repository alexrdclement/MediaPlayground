package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import kotlinx.io.files.Path

interface MediaMetadataRetriever {
    suspend fun getMediaMetadata(
        contentUri: Uri,
        onEmbeddedPictureFound: suspend (ByteArray) -> Path?,
    ): MediaMetadata
}
