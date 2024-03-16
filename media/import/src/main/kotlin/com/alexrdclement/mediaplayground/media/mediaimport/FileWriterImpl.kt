package com.alexrdclement.mediaplayground.media.mediaimport

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.alexrdclement.mediaplayground.model.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import javax.inject.Inject

class FileWriterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileWriter {

    override suspend fun writeBitmapToDisk(
        byteArray: ByteArray,
        destination: Path,
    ): Result<Path, FileWriteError> {
        return byteArray.writeToDisk(destination)
    }

    override suspend fun writeToDisk(
        contentUri: Uri,
        destinationDir: Path
    ): Result<Path, FileWriteError> {
        val documentFile = DocumentFile.fromSingleUri(context, contentUri)
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)
        val documentFileName = documentFile.name
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)

        return documentFile.writeToDisk(
            destination = Path(destinationDir, documentFileName),
            contentResolver = context.contentResolver,
        )
    }
}

suspend fun ByteArray.writeToDisk(destination: Path): Result<Path, FileWriteError> =
    withContext(Dispatchers.IO) {
        try {
            SystemFileSystem.sink(destination).buffered().use { sink ->
                sink.write(this@writeToDisk)
            }
            Result.Success(destination)
        } catch(e: FileNotFoundException) {
            Result.Failure(FileWriteError.InputFileNotFound(e))
        } catch(e: IOException) {
            Result.Failure(FileWriteError.Unknown(e))
        } catch (e: Throwable) {
            Result.Failure(FileWriteError.Unknown(e))
        }
    }

suspend fun DocumentFile.writeToDisk(
    destination: Path,
    contentResolver: ContentResolver,
): Result<Path, FileWriteError> = withContext(Dispatchers.IO) {
    try {
        val inputStream = contentResolver.openInputStream(this@writeToDisk.uri)
            ?: return@withContext Result.Failure(FileWriteError.InputStreamError)
        inputStream.asSource().buffered().use { source ->
            SystemFileSystem.sink(destination).buffered().use { sink ->
                source.transferTo(sink)
            }
        }
        Result.Success(destination)
    } catch(e: FileNotFoundException) {
        Result.Failure(FileWriteError.InputFileNotFound(e))
    } catch(e: IOException) {
        Result.Failure(FileWriteError.Unknown(e))
    } catch (e: Throwable) {
        Result.Failure(FileWriteError.Unknown(e))
    }
}
