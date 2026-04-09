package com.alexrdclement.mediaplayground.media.store

import android.net.Uri
import com.alexrdclement.mediaplayground.model.result.Result

sealed class FileReadError {
    data object InputStreamError : FileReadError()
    data class InputFileNotFound(val throwable: Throwable) : FileReadError()
    data class Unknown(val throwable: Throwable?) : FileReadError()
}

interface FileReader {
    suspend fun readBytes(uri: Uri): Result<ByteArray, FileReadError>
}
