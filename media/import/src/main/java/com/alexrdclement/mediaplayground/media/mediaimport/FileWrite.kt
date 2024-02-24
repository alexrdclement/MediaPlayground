package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.ContentResolver
import android.graphics.Bitmap
import androidx.documentfile.provider.DocumentFile
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File

sealed class FileWriteError {
    data object InputStreamError : FileWriteError()
    data class Unknown(val throwable: Throwable?) : FileWriteError()
}

suspend fun Bitmap.writeToDisk(destination: File): Result<File, FileWriteError> =
    withContext(Dispatchers.IO) {
        try {
            destination.outputStream().use { outputStream ->
                this@writeToDisk.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            Result.Success(destination)
        } catch (e: Throwable) {
            Result.Failure(FileWriteError.Unknown(e))
        }
    }

suspend fun DocumentFile.writeToDisk(
    destination: File,
    contentResolver: ContentResolver,
): Result<File, FileWriteError> = withContext(Dispatchers.IO) {
    try {
        val inputStream = contentResolver.openInputStream(uri)
            ?: return@withContext Result.Failure(FileWriteError.InputStreamError)
        inputStream.source().use { source ->
            destination.sink().buffer().use {
                it.writeAll(source)
            }
        }
        Result.Success(destination)
    } catch (e: Throwable) {
        Result.Failure(FileWriteError.Unknown(e))
    }
}
