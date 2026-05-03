package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.store.FileWriteError
import com.alexrdclement.mediaplayground.media.store.FileWriter
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

class FakeFileWriter : FileWriter {

    override suspend fun write(
        byteArray: ByteArray,
        destination: Path,
    ): Result<Path, FileWriteError> {
        return Result.Success(value = destination)
    }
}
