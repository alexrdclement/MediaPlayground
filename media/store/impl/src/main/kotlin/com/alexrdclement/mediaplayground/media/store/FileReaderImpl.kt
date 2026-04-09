package com.alexrdclement.mediaplayground.media.store

import android.app.Application
import android.net.Uri
import com.alexrdclement.mediaplayground.model.result.Result
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.FileNotFoundException

class FileReaderImpl @Inject constructor(
    private val application: Application,
) : FileReader {

    override suspend fun readBytes(uri: Uri): Result<ByteArray, FileReadError> =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = application.contentResolver.openInputStream(uri)
                    ?: return@withContext Result.Failure(FileReadError.InputStreamError)
                Result.Success(inputStream.use { it.readBytes() })
            } catch (e: FileNotFoundException) {
                Result.Failure(FileReadError.InputFileNotFound(e))
            } catch (e: IOException) {
                Result.Failure(FileReadError.Unknown(e))
            } catch (e: Throwable) {
                Result.Failure(FileReadError.Unknown(e))
            }
        }
}
