package com.alexrdclement.mediaplayground.media.mediaimport.mapper

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.store.FileWriteError

fun FileWriteError.toMediaImportError() = when (this) {
    FileWriteError.UnknownInputFileError -> MediaImportError.InputFileError
    is FileWriteError.InputFileNotFound ->
        MediaImportError.FileWriteError.InputFileNotFound(throwable = throwable)
    FileWriteError.InputStreamError -> MediaImportError.FileWriteError.InputStreamError
    is FileWriteError.Unknown -> MediaImportError.FileWriteError.Unknown(throwable = throwable)
}
