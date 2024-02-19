package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.ContentResolver.MimeTypeInfo
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.annotation.OptIn
import androidx.documentfile.provider.DocumentFile
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import com.alexrdclement.mediaplayground.media.mediaimport.mapper.toTrack
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportFailure
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

@OptIn(UnstableApi::class)
class MediaImporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mediaMetadataResolver: MediaMetadataResolver,
) {

    private companion object {
        const val PNG_EXTENSION = "png"
    }

    /**
     * Import audio file as a track from disk.
     * @param uri: URI of the file as given by the document provider (not MediaStore).
     * @param fileWriteDir: Where to write files associated with this media.
     */
    suspend fun importTrackFromDisk(
        uri: Uri,
        fileWriteDir: File,
    ): Result<Track, MediaImportFailure> = withContext(Dispatchers.IO) {
        try {
            val mediaItem = MediaItem.fromUri(uri)
            val mediaMetadata = mediaMetadataResolver.getMediaMetadata(
                contentUri = uri,
                onEmbeddedPictureFound = { embeddedPicture ->
                    val file = File(fileWriteDir, getFileName(uri))
                    val bitmap = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size)
                    if (bitmap?.writeToDisk(file) != true) {
                        null
                    } else {
                        file
                    }
                },
            )
            Result.Success(
                value = mediaItem.toTrack(
                    contentUri = uri,
                    mediaMetadata = mediaMetadata
                )
            )
        } catch (e: Throwable) {
            Result.Failure(MediaImportFailure.Unknown(throwable = e))
        }
    }

    private fun getFileName(uri: Uri): String {
        val documentFileName = DocumentFile.fromSingleUri(context, uri)?.name
        val fileName = documentFileName?.let(::File)?.nameWithoutExtension
            ?: UUID.randomUUID().toString()
        return "$fileName.$PNG_EXTENSION"
    }

    private suspend fun Bitmap.writeToDisk(file: File) = withContext(Dispatchers.IO) {
        file.outputStream().use {
            this@writeToDisk.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
