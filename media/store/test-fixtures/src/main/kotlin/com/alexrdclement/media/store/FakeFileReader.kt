package com.alexrdclement.media.store

import android.net.Uri
import com.alexrdclement.mediaplayground.media.store.FileReadError
import com.alexrdclement.mediaplayground.media.store.FileReader
import com.alexrdclement.mediaplayground.model.result.Result

class FakeFileReader : FileReader {

    override suspend fun readBytes(uri: Uri): Result<ByteArray, FileReadError> {
        return Result.Success(uri.toString().encodeToByteArray())
    }
}
