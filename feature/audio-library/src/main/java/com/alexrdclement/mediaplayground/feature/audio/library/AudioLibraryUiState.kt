package com.alexrdclement.mediaplayground.feature.audio.library

import androidx.paging.PagingData
import com.alexrdclement.ui.shared.model.MediaItemUi
import kotlinx.coroutines.flow.Flow

sealed class AudioLibraryUiState {
    data object InitialState : AudioLibraryUiState()

    data class ContentReady(
        val spotifyContentState: SpotifyContentState,
        val isMediaItemLoaded: Boolean,
    ) : AudioLibraryUiState() {

        sealed class SpotifyContentState {
            data object NotLoggedIn : SpotifyContentState()
            data class LoggedIn(
                val savedTracks: Flow<PagingData<MediaItemUi>>,
                val savedAlbums: Flow<PagingData<MediaItemUi>>,
            ) : SpotifyContentState()
        }
    }
}
