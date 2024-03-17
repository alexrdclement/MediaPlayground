package com.alexrdclement.mediaplayground.media.mediaimport

import android.net.Uri
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

sealed class FileWriteError {
    data object UnknownInputFileError : FileWriteError()
    data class InputFileNotFound(val throwable: Throwable) : FileWriteError()
    data object InputStreamError : FileWriteError()
    data class Unknown(val throwable: Throwable?) : FileWriteError()
}

interface FileWriter {

    suspend fun writeBitmapToDisk(
        byteArray: ByteArray,
        destination: Path,
    ): Result<Path, FileWriteError>

    suspend fun writeToDisk(
        contentUri: Uri,
        destinationDir: Path
    ): Result<Path, FileWriteError>
}
