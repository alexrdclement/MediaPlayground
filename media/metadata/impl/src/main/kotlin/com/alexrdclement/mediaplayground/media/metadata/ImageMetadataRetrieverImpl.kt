package com.alexrdclement.mediaplayground.media.metadata

import android.app.Application
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import dev.zacsweers.metro.Inject

class ImageMetadataRetrieverImpl @Inject constructor(
    private val application: Application,
) {

    fun getImageMetadata(
        contentUri: Uri,
        mimeType: String,
        extension: String,
    ): MediaMetadata.Image {
        val inputStream = application.contentResolver.openInputStream(contentUri)
        val exif = inputStream?.use { ExifInterface(it) }
        val latLong = exif?.getLatLong()
        return MediaMetadata.Image(
            widthPx = exif?.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)?.takeIf { it > 0 },
            heightPx = exif?.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)?.takeIf { it > 0 },
            dateTimeOriginal = exif?.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL),
            gpsLatitude = latLong?.getOrNull(0),
            gpsLongitude = latLong?.getOrNull(1),
            cameraMake = exif?.getAttribute(ExifInterface.TAG_MAKE),
            cameraModel = exif?.getAttribute(ExifInterface.TAG_MODEL),
            mimeType = mimeType,
            extension = extension,
        )
    }
}
