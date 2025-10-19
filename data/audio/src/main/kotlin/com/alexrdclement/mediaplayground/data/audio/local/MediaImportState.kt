package com.alexrdclement.mediaplayground.data.audio.local

import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.loggable.Loggable

sealed class MediaImportState {
    object InProgress : MediaImportState()
    data class Completed(val result: MediaImportResult) : MediaImportState()
}

sealed class MediaImportResult : Loggable {
    data class Success(val track: Track) : MediaImportResult()
    data class Failure(val error: Error) : MediaImportResult()

    override val message: String
        get() = when (this) {
            is Success -> "Import success: ${track.title}"
            is Failure -> error.message
        }

    sealed class Error : Loggable {
        data class ImportError(val error: MediaImportError) : Error()
        data class Unknown(override val throwable: Throwable) : Error()

        override val message: String
            get() = when (this) {
                is ImportError -> "Import error: ${error.message}"
                is Unknown -> "Unknown error: ${throwable.message}"
            }
    }
}

