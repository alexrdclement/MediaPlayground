package com.alexrdclement.mediaplayground.media.metadata

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import dev.zacsweers.metro.Inject

class MediaMetadataRetrieverImpl @Inject constructor(
    private val application: Application,
    private val audioMetadataRetriever: AudioMetadataRetrieverImpl,
    private val imageMetadataRetriever: ImageMetadataRetrieverImpl,
) : MediaMetadataRetriever {

    override suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata {
        val mimeType = application.contentResolver.getType(contentUri)
        val extension = mimeType
            ?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
            ?: "jpg"
        return when {
            mimeType == null -> error("No mime type for $contentUri")
            mimeType.startsWith("image/") -> imageMetadataRetriever.getImageMetadata(
                contentUri = contentUri,
                mimeType = mimeType,
                extension = extension,
            )

            else -> audioMetadataRetriever.getAudioMetadata(
                contentUri = contentUri,
                mimeType = mimeType,
                extension = extension,
            )
        }
    }
}
