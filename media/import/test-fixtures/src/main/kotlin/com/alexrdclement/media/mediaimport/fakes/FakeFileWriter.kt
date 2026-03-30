package com.alexrdclement.media.mediaimport.fakes

import android.net.Uri
import com.alexrdclement.mediaplayground.media.mediaimport.FileWriteError
import com.alexrdclement.mediaplayground.media.mediaimport.FileWriter
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.io.files.Path

class FakeFileWriter : FileWriter {

    override suspend fun writeBitmapToDisk(
        byteArray: ByteArray,
        destination: Path
    ): Result<Path, FileWriteError> {
        return Result.Success(value = destination)
    }

    override suspend fun writeToDisk(
        contentUri: Uri,
        destinationDir: Path
    ): Result<Path, FileWriteError> {
        return Result.Success(value = destinationDir)
    }

    override suspend fun writeFileToDisk(
        contentUri: Uri,
        destination: Path
    ): Result<Path, FileWriteError> {
        return Result.Success(value = destination)
    }
}
