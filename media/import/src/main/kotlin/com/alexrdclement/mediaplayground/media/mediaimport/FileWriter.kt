package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.alexrdclement.mediaplayground.model.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

sealed class FileWriteError {
    data object UnknownInputFileError : FileWriteError()
    data class InputFileNotFound(val throwable: Throwable) : FileWriteError()
    data object InputStreamError : FileWriteError()
    data class Unknown(val throwable: Throwable?) : FileWriteError()
}

class FileWriter @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    suspend fun writeBitmapToDisk(
        byteArray: ByteArray,
        destination: File,
    ): Result<File, FileWriteError> {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            ?: return Result.Failure(FileWriteError.InputStreamError)
        return bitmap.writeToDisk(destination)
    }

    suspend fun writeToDisk(
        contentUri: Uri,
        destinationDir: File
    ): Result<File, FileWriteError> {
        val documentFile = DocumentFile.fromSingleUri(context, contentUri)
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)
        val documentFileName = documentFile.name
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)

        return documentFile.writeToDisk(
            destination = File(destinationDir, documentFileName),
            contentResolver = context.contentResolver,
        )
    }
}

suspend fun Bitmap.writeToDisk(destination: File): Result<File, FileWriteError> =
    withContext(Dispatchers.IO) {
        try {
            destination.outputStream().use { outputStream ->
                this@writeToDisk.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            }
            Result.Success(destination)
        } catch(e: FileNotFoundException) {
            Result.Failure(FileWriteError.InputFileNotFound(e))
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
    } catch(e: FileNotFoundException) {
        Result.Failure(FileWriteError.InputFileNotFound(e))
    } catch (e: Throwable) {
        Result.Failure(FileWriteError.Unknown(e))
    }
}
