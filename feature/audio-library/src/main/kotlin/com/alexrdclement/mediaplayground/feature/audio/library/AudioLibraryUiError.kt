package com.alexrdclement.mediaplayground.feature.audio.library

import android.net.Uri
import com.alexrdclement.mediaplayground.data.audio.local.MediaImportResult
import com.alexrdclement.uiplayground.loggable.Loggable

sealed class AudioLibraryUiError : Loggable {
    data class ImportFailure(
        val uri: Uri,
        val error: MediaImportResult.Error,
    ) : AudioLibraryUiError()
    data object NotPlayable : AudioLibraryUiError()
    data object SpotifyNotAuthenticated: AudioLibraryUiError()

    override val message: String
        get() = when (this) {
            is ImportFailure -> when (error) {
                is MediaImportResult.Error.Unknown ->
                    "Unknown error importing item: ${error.throwable.message}"
                is MediaImportResult.Error.ImportError -> "Error importing item: ${error.message}"
            }
            NotPlayable -> "Item not playable"
            SpotifyNotAuthenticated -> "Spotify not authenticated"
        }

    override val throwable: Throwable?
        get() = when (this) {
            is ImportFailure -> when (error) {
                is MediaImportResult.Error.Unknown -> error.throwable
                is MediaImportResult.Error.ImportError -> null
            }
            NotPlayable -> null
            SpotifyNotAuthenticated -> null
        }
}
