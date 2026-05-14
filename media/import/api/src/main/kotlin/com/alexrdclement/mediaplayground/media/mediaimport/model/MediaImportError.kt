package com.alexrdclement.mediaplayground.media.mediaimport.model

import com.alexrdclement.logging.Loggable


sealed class MediaImportError : Loggable {
    data object MkdirError : MediaImportError()
    data object InputFileError : MediaImportError()
    sealed class FileWriteError : MediaImportError() {
        data class InputFileNotFound(override val throwable: Throwable) : FileWriteError()
        data class Unknown(override val throwable: Throwable? = null) : FileWriteError()
    }
    data class Unknown(override val throwable: Throwable? = null) : MediaImportError()

    override val message: String
        get() = when (this) {
            MkdirError -> "Error creating directory for media"
            InputFileError -> "Error with input file"
            is FileWriteError -> when (this) {
                is FileWriteError.InputFileNotFound -> "Input file not found: ${throwable.message}"
                is FileWriteError.Unknown -> "Unknown file write error: ${throwable?.message}"
            }
            is Unknown -> "Unknown error: ${throwable?.message}"
        }
}
