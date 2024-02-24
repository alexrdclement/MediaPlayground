package com.alexrdclement.mediaplayground.media.mediaimport.model

sealed class MediaImportError {
    data object InputFileError : MediaImportError()
    sealed class FileWriteError : MediaImportError() {
        data object InputStreamError : FileWriteError()
        data class Unknown(val throwable: Throwable? = null) : FileWriteError()
    }
    data class Unknown(val throwable: Throwable? = null) : MediaImportError()
}
