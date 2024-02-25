package com.alexrdclement.mediaplayground.feature.spotify

import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi
import kotlinx.coroutines.flow.Flow

sealed class SpotifyLibraryUiState {
    data object InitialState : SpotifyLibraryUiState()

    data object NotLoggedIn : SpotifyLibraryUiState()

    data class LoggedIn(
        val savedTracks: Flow<PagingData<MediaItemUi>>,
        val savedAlbums: Flow<PagingData<MediaItemUi>>,
        val isMediaItemLoaded: Boolean,
    ) : SpotifyLibraryUiState()
}
