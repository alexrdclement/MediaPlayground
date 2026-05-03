package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

sealed class FileWriteError {
    data class InputFileNotFound(val throwable: Throwable) : FileWriteError()
    data class MkdirError(val throwable: Throwable) : FileWriteError()
    data class Unknown(val throwable: Throwable?) : FileWriteError()
}

interface FileWriter {
    suspend fun write(byteArray: ByteArray, destination: Path): Result<Path, FileWriteError>
}
