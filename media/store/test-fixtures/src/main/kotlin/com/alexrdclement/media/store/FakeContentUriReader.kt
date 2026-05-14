package com.alexrdclement.media.store

import android.net.Uri
import com.alexrdclement.mediaplayground.media.store.ContentUriReadError
import com.alexrdclement.mediaplayground.media.store.ContentUriReader
import com.alexrdclement.mediaplayground.model.result.Result

class FakeContentUriReader : ContentUriReader {

    override suspend fun readBytes(uri: Uri): Result<ByteArray, ContentUriReadError> {
        return Result.Success(uri.toString().encodeToByteArray())
    }
}
