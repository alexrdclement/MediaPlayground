package com.alexrdclement.mediaplayground.media.store

import android.net.Uri
import com.alexrdclement.mediaplayground.model.result.Result

sealed class ContentUriReadError {
    data object InputStreamError : ContentUriReadError()
    data class InputFileNotFound(val throwable: Throwable) : ContentUriReadError()
    data class Unknown(val throwable: Throwable?) : ContentUriReadError()
}

interface ContentUriReader {
    suspend fun readBytes(uri: Uri): Result<ByteArray, ContentUriReadError>
}
