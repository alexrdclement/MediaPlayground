package com.alexrdclement.mediaplayground.media.mediaimport

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import dev.zacsweers.metro.Inject
import android.media.MediaMetadataRetriever as AndroidMediaMetadataRetriever

class MediaMetadataRetrieverImpl @Inject constructor(
    private val application: Application,
) : MediaMetadataRetriever {

    override suspend fun getMediaMetadata(contentUri: Uri): MediaMetadata {
        val mimeType = application.contentResolver.getType(contentUri)
        val extension = mimeType
            ?.let { MimeTypeMap.getSingleton().getExtensionFromMimeType(it) }
            ?: "jpg"

        if (mimeType?.startsWith("image/") == true) {
            return MediaMetadata(
                title = null,
                durationMs = null,
                trackNumber = null,
                artistName = null,
                albumTitle = null,
                embeddedPicture = null,
                mimeType = mimeType,
                extension = extension,
            )
        }

        return AndroidMediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(application, contentUri)
            MediaMetadata(
                title = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_TITLE),
                durationMs = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLongOrNull(),
                trackNumber = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)
                    ?.toIntOrNull(),
                artistName = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ARTIST),
                albumTitle = retriever.extractMetadata(AndroidMediaMetadataRetriever.METADATA_KEY_ALBUM),
                embeddedPicture = retriever.embeddedPicture,
                mimeType = mimeType,
                extension = extension,
            )
        }
    }
}
