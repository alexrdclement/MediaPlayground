package com.alexrdclement.mediaplayground.media.store

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.alexrdclement.mediaplayground.model.result.Result
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.CoroutineDispatcher
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
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FileWriter {

    override suspend fun writeBitmapToDisk(
        byteArray: ByteArray,
        destination: Path,
    ): Result<Path, FileWriteError> {
        return byteArray.writeToDisk(destination = destination)
    }

    override suspend fun writeFileToDisk(
        contentUri: Uri,
        destination: Path,
    ): Result<Path, FileWriteError> {
        return contentUri.writeToDisk(
            destination = destination,
            contentResolver = application.contentResolver,
        )
    }

    override suspend fun writeToDisk(
        contentUri: Uri,
        destinationDir: Path,
    ): Result<Path, FileWriteError> {
        val documentFile = DocumentFile.fromSingleUri(application, contentUri)
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)
        val documentFileName = documentFile.name
            ?: return Result.Failure(FileWriteError.UnknownInputFileError)

        return documentFile.uri.writeToDisk(
            destination = Path(destinationDir, documentFileName),
            contentResolver = application.contentResolver,
        )
    }

    private suspend fun ByteArray.writeToDisk(destination: Path): Result<Path, FileWriteError> =
        withContext(ioDispatcher) {
            catchingFileWriteErrors {
                SystemFileSystem.sink(destination).buffered().use { sink ->
                    sink.write(this@writeToDisk)
                }
                Result.Success(destination)
            }
        }

    private suspend fun Uri.writeToDisk(
        destination: Path,
        contentResolver: ContentResolver,
    ): Result<Path, FileWriteError> = withContext(ioDispatcher) {
        catchingFileWriteErrors {
            val inputStream = contentResolver.openInputStream(this@writeToDisk)
                ?: return@withContext Result.Failure(FileWriteError.InputStreamError)
            inputStream.asSource().buffered().use { source ->
                SystemFileSystem.sink(destination).buffered().use { sink ->
                    source.transferTo(sink)
                }
            }
            Result.Success(destination)
        }
    }
}

private inline fun catchingFileWriteErrors(block: () -> Result<Path, FileWriteError>): Result<Path, FileWriteError> =
    try {
        block()
    } catch (e: FileNotFoundException) {
        Result.Failure(FileWriteError.InputFileNotFound(e))
    } catch (e: IOException) {
        Result.Failure(FileWriteError.Unknown(e))
    } catch (e: SecurityException) {
        Result.Failure(FileWriteError.Unknown(e))
    } catch (e: IllegalArgumentException) {
        Result.Failure(FileWriteError.Unknown(e))
    }
