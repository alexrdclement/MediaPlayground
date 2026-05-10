package com.alexrdclement.mediaplayground.data.track

import com.alexrdclement.logging.Loggable
import com.alexrdclement.mediaplayground.media.mediaimport.model.MediaImportError
import com.alexrdclement.mediaplayground.media.model.AudioTrack

sealed class TrackImportState {
    object InProgress : TrackImportState()
    data class Completed(val result: TrackImportResult) : TrackImportState()
}

sealed class TrackImportResult : Loggable {
    data class Success(val track: AudioTrack) : TrackImportResult()
    data class Failure(val error: Error) : TrackImportResult()

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
