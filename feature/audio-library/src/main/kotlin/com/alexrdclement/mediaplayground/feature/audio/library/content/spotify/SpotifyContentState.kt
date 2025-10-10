package com.alexrdclement.mediaplayground.feature.audio.library.content.spotify

import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import kotlinx.coroutines.flow.Flow

sealed class SpotifyContentState {
    data object NotLoggedIn : SpotifyContentState()
    data class LoggedIn(
        val savedTracks: Flow<PagingData<MediaItemUi>>,
        val savedAlbums: Flow<PagingData<MediaItemUi>>,
    ) : SpotifyContentState()
}
