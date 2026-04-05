package com.alexrdclement.mediaplayground.media.metadata

import android.app.Application
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.alexrdclement.mediaplayground.media.metadata.model.MediaMetadata
import com.alexrdclement.mediaplayground.media.model.ImageMetadata
import dev.zacsweers.metro.Inject

class ImageMetadataRetrieverImpl @Inject constructor(
    private val application: Application,
) {

    fun getImageMetadata(
        contentUri: Uri,
        mimeType: String?,
        extension: String,
    ): MediaMetadata.Image {
        return MediaMetadata.Image(
            imageMetadata = extractImageMetadata(contentUri),
            mimeType = mimeType,
            extension = extension,
        )
    }

    private fun extractImageMetadata(
        contentUri: Uri,
    ): ImageMetadata? {
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
