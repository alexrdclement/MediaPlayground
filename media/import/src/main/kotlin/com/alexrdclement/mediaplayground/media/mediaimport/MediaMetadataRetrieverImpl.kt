package com.alexrdclement.mediaplayground.media.mediaimport

import android.app.Application
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.exifinterface.media.ExifInterface
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.image.ImageMetadata
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
            return MediaMetadata.Image(
                imageMetadata = extractImageMetadata(contentUri),
                mimeType = mimeType,
                extension = extension,
            )
        }

        return AndroidMediaMetadataRetriever().use { retriever ->
            retriever.setDataSource(application, contentUri)
            MediaMetadata.Audio(
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

    private fun extractImageMetadata(contentUri: Uri): ImageMetadata? {
        val inputStream = application.contentResolver.openInputStream(contentUri) ?: return null
        return inputStream.use { stream ->
            val exif = ExifInterface(stream)
            val latLong = exif.getLatLong()
            ImageMetadata(
                widthPx = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
                    .takeIf { it > 0 },
                heightPx = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)
                    .takeIf { it > 0 },
                dateTimeOriginal = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL),
                gpsLatitude = latLong?.getOrNull(0),
                gpsLongitude = latLong?.getOrNull(1),
                cameraMake = exif.getAttribute(ExifInterface.TAG_MAKE),
                cameraModel = exif.getAttribute(ExifInterface.TAG_MODEL),
            ).takeUnless { it == ImageMetadata() }
        }
    }
}
