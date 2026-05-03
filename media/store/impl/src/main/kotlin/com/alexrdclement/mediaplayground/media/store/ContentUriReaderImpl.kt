package com.alexrdclement.mediaplayground.media.store

import android.app.Application
import android.net.Uri
import com.alexrdclement.mediaplayground.model.result.Result
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import kotlinx.io.files.FileNotFoundException

class ContentUriReaderImpl @Inject constructor(
    private val application: Application,
) : ContentUriReader {

    override suspend fun readBytes(uri: Uri): Result<ByteArray, ContentUriReadError> =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = application.contentResolver.openInputStream(uri)
                    ?: return@withContext Result.Failure(ContentUriReadError.InputStreamError)
                Result.Success(inputStream.use { it.readBytes() })
            } catch (e: FileNotFoundException) {
                Result.Failure(ContentUriReadError.InputFileNotFound(e))
            } catch (e: IOException) {
                Result.Failure(ContentUriReadError.Unknown(e))
            } catch (e: Throwable) {
                Result.Failure(ContentUriReadError.Unknown(e))
            }
        }
}
