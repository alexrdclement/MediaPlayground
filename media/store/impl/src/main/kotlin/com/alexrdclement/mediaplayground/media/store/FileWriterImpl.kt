package com.alexrdclement.mediaplayground.media.store

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class FileWriterImpl @Inject constructor(
    private val application: Application,
) : FileWriter {

    override suspend fun writeBitmapToDisk(
        byteArray: ByteArray,
        destination: Path,
    ): Result<Path, FileWriteError> {
        destination.ensureParentDir().guardSuccess { return Result.Failure(it) }
        return byteArray.writeToDisk(destination)
    }

    override suspend fun writeFileToDisk(
        contentUri: Uri,
        destination: Path,
    ): Result<Path, FileWriteError> {
        destination.ensureParentDir().guardSuccess { return Result.Failure(it) }
        return contentUri.writeToDisk(destination, application.contentResolver)
    }

    override suspend fun writeToDisk(
        contentUri: Uri,
        destinationDir: Path,
    ): Result<Path, FileWriteError> {
        val documentFile = DocumentFile.fromSingleUri(application, contentUri)
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)
        val documentFileName = documentFile.name
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)

        val destination = Path(destinationDir, documentFileName)
        destination.ensureParentDir().guardSuccess { return Result.Failure(it) }
        return documentFile.uri.writeToDisk(
            destination = destination,
            contentResolver = application.contentResolver,
        )
    }
}

private suspend fun Path.ensureParentDir(): Result<Unit, FileWriteError> =
    withContext(Dispatchers.IO) {
        val parent = parent ?: return@withContext Result.Success(Unit)
        try {
            SystemFileSystem.createDirectories(parent)
            Result.Success(Unit)
        } catch (e: IOException) {
            Result.Failure(FileWriteError.MkdirError(e))
        } catch (e: Throwable) {
            Result.Failure(FileWriteError.MkdirError(e))
        }
    }

suspend fun ByteArray.writeToDisk(destination: Path): Result<Path, FileWriteError> =
    withContext(Dispatchers.IO) {
        try {
            SystemFileSystem.sink(destination).buffered().use { sink ->
                sink.write(this@writeToDisk)
            }
            Result.Success(destination)
        } catch (e: FileNotFoundException) {
            Result.Failure(FileWriteError.InputFileNotFound(e))
        } catch (e: IOException) {
            Result.Failure(FileWriteError.Unknown(e))
        } catch (e: Throwable) {
            Result.Failure(FileWriteError.Unknown(e))
        }
    }

private suspend fun Uri.writeToDisk(
    destination: Path,
    contentResolver: ContentResolver,
): Result<Path, FileWriteError> = withContext(Dispatchers.IO) {
    try {
        val inputStream = contentResolver.openInputStream(this@writeToDisk)
            ?: return@withContext Result.Failure(FileWriteError.InputStreamError)
        inputStream.asSource().buffered().use { source ->
            SystemFileSystem.sink(destination).buffered().use { sink ->
                source.transferTo(sink)
            }
        }
        Result.Success(destination)
    } catch (e: FileNotFoundException) {
        Result.Failure(FileWriteError.InputFileNotFound(e))
    } catch (e: IOException) {
        Result.Failure(FileWriteError.Unknown(e))
    } catch (e: Throwable) {
        Result.Failure(FileWriteError.Unknown(e))
    }
}
