package com.alexrdclement.mediaplayground.feature.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.mediasession.mapper.toMediaItem
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.cache
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SpotifyLibraryViewModel @Inject constructor(
    private val spotifyAuth: SpotifyAuth,
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
) : ViewModel() {

    private companion object {
        val pagingConfig = PagingConfig(pageSize = 10)
    }

    private val savedTracksPager = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedTracksPagingSource,
    )
    private val savedTracks: Flow<PagingData<MediaItem>> = savedTracksPager.flow
        .map { pagingData ->
            pagingData.map { track -> track as MediaItem }
        }.cachedIn(viewModelScope)

    private val savedAlbumsPager = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedAlbumsPagingSource,
    )
    private val savedAlbums = savedAlbumsPager.flow
        .map { pagingData ->
            pagingData.map { album -> album as MediaItem }
        }.cachedIn(viewModelScope)

    val uiState: StateFlow<SpotifyLibraryUiState> = spotifyAuth.isLoggedIn.map { isLoggedIn ->
        if (!isLoggedIn) {
            return@map SpotifyLibraryUiState.NotLoggedIn
        }

        return@map SpotifyLibraryUiState.LoggedIn(
            savedTracks = savedTracks,
            savedAlbums = savedAlbums,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SpotifyLibraryUiState.InitialState
    )

    fun onLogOutClick() {
        spotifyAuth.logOut()
    }

    fun onPlayTrack(track: Track) {
        val mediaItem = track.toMediaItem()
        val player = mediaSessionManager.player.value ?: return
        with(player) {
            setMediaItem(mediaItem)
            play()
        }
    }
}
