package com.alexrdclement.mediaplayground.feature.spotify

import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow

sealed class SpotifyLibraryUiState {
    data object InitialState : SpotifyLibraryUiState()

    data object NotLoggedIn : SpotifyLibraryUiState()

    data class LoggedIn(
        val savedTracks: Flow<PagingData<MediaItem>>,
        val savedAlbums: Flow<PagingData<MediaItem>>
    ) : SpotifyLibraryUiState()
}
