package com.alexrdclement.mediaplayground.media.mediaimport.model

sealed class MediaImportError {
    data object MkdirError : MediaImportError()
    data object InputFileError : MediaImportError()
    sealed class FileWriteError : MediaImportError() {
        data class InputFileNotFound(val throwable: Throwable) : FileWriteError()
        data object InputStreamError : FileWriteError()
        data class Unknown(val throwable: Throwable? = null) : FileWriteError()
    }
    data class Unknown(val throwable: Throwable? = null) : MediaImportError()
}
