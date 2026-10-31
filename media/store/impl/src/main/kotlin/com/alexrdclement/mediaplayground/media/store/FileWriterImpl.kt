package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.buffered
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem

class FileWriterImpl @Inject constructor() : FileWriter {

    override suspend fun write(
        byteArray: ByteArray,
        destination: Path,
    ): Result<Path, FileWriteError> {
        destination.ensureParentDir().guardSuccess { return Result.Failure(it) }
        return byteArray.writeToDisk(destination)
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

private suspend fun ByteArray.writeToDisk(destination: Path): Result<Path, FileWriteError> =
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
