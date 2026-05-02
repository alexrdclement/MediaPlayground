package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.store.FileReadError
import com.alexrdclement.mediaplayground.media.store.FileWriteError
import com.alexrdclement.mediaplayground.model.result.Result

fun FileReadError.toMediaImportError() = when (this) {
    FileReadError.InputStreamError -> MediaImportError.InputFileError
    is FileReadError.InputFileNotFound ->
        MediaImportError.FileWriteError.InputFileNotFound(throwable = throwable)
    is FileReadError.Unknown -> MediaImportError.Unknown(throwable = throwable)
}

fun FileWriteError.toMediaImportError() = when (this) {
    FileWriteError.UnknownInputFileError -> MediaImportError.InputFileError
    is FileWriteError.InputFileNotFound ->
        MediaImportError.FileWriteError.InputFileNotFound(throwable = throwable)
    FileWriteError.InputStreamError -> MediaImportError.FileWriteError.InputStreamError
    is FileWriteError.MkdirError -> MediaImportError.MkdirError
    is FileWriteError.Unknown -> MediaImportError.FileWriteError.Unknown(throwable = throwable)
}

fun <T> Result.Failure<*, MediaImportError>.mapFailure(): Result<T, MediaImportError> =
    Result.Failure(this.failure)
