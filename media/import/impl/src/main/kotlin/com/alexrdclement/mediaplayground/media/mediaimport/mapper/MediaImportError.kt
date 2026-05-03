package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.store.ContentUriReadError
import com.alexrdclement.mediaplayground.media.store.FileWriteError
import com.alexrdclement.mediaplayground.model.result.Result

fun ContentUriReadError.toMediaImportError() = when (this) {
    ContentUriReadError.InputStreamError -> MediaImportError.InputFileError
    is ContentUriReadError.InputFileNotFound ->
        MediaImportError.FileWriteError.InputFileNotFound(throwable = throwable)
    is ContentUriReadError.Unknown -> MediaImportError.Unknown(throwable = throwable)
}

fun FileWriteError.toMediaImportError() = when (this) {
    is FileWriteError.InputFileNotFound ->
        MediaImportError.FileWriteError.InputFileNotFound(throwable = throwable)
    is FileWriteError.MkdirError -> MediaImportError.MkdirError
    is FileWriteError.Unknown -> MediaImportError.FileWriteError.Unknown(throwable = throwable)
}

fun <T> Result.Failure<*, MediaImportError>.mapFailure(): Result<T, MediaImportError> =
    Result.Failure(this.failure)
