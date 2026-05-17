package com.alexrdclement.mediaplayground.media.metadata

import android.app.Application
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import com.alexrdclement.mediaplayground.media.model.MediaMetadata
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path

class ImageMetadataRetrieverImpl @Inject constructor(
    private val application: Application,
) {

    suspend fun getImageMetadata(
        filePath: Path,
        mimeType: String,
        extension: String,
    ): MediaMetadata.Image = withContext(Dispatchers.IO) {
        val exif = runCatching { ExifInterface(filePath.toString()) }.getOrNull()
        val (widthPx, heightPx) = exif?.getDimensPx() ?: getBitmapDimensPx { options ->
            BitmapFactory.decodeFile(filePath.toString(), options)
        }
        buildImageMetadata(exif, widthPx, heightPx, mimeType, extension)
    }

    suspend fun getImageMetadata(
        contentUri: Uri,
        mimeType: String,
        extension: String,
    ): MediaMetadata.Image = withContext(Dispatchers.IO) {
        val inputStream = application.contentResolver.openInputStream(contentUri)
        val exif = inputStream?.use { ExifInterface(it) }
        val (widthPx, heightPx) = exif?.getDimensPx() ?: getBitmapDimensPx { options ->
            application.contentResolver.openInputStream(contentUri)?.use { stream ->
                BitmapFactory.decodeStream(stream, null, options)
            }
        }
        buildImageMetadata(exif, widthPx, heightPx, mimeType, extension)
    }

    private fun buildImageMetadata(
        exif: ExifInterface?,
        widthPx: Int,
        heightPx: Int,
        mimeType: String,
        extension: String,
    ): MediaMetadata.Image {
        val latLong = exif?.getLatLong()
        return MediaMetadata.Image(
            widthPx = widthPx,
            heightPx = heightPx,
            dateTimeOriginal = exif?.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL),
            gpsLatitude = latLong?.getOrNull(0),
            gpsLongitude = latLong?.getOrNull(1),
            cameraMake = exif?.getAttribute(ExifInterface.TAG_MAKE),
            cameraModel = exif?.getAttribute(ExifInterface.TAG_MODEL),
            mimeType = mimeType,
            extension = extension,
        )
    }

    private fun ExifInterface.getDimensPx(): Pair<Int, Int>? {
        val width = getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).takeIf { it > 0 }
        val height = getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).takeIf { it > 0 }
        return if (width != null && height != null) width to height else null
    }

    private fun getBitmapDimensPx(decode: (BitmapFactory.Options) -> Unit): Pair<Int, Int> {
        return with(BitmapFactory.Options()) {
            inJustDecodeBounds = true
            decode(this)
            val width = outWidth.takeIf { it > 0 } ?: error("Could not determine image width")
            val height = outHeight.takeIf { it > 0 } ?: error("Could not determine image height")
            width to height
        }
    }
}
