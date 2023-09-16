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
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.shared.model.MediaItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    private val savedTracks: Flow<PagingData<MediaItemUi>> = combine(
        savedTracksPager.flow.cachedIn(viewModelScope),
        mediaSessionManager.loadedMediaItem,
        mediaSessionManager.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { track ->
            MediaItemUi(
                mediaItem = track,
                isPlaying = isPlaying && track.id == loadedMediaItem?.id
            )
        }
    }.cachedIn(viewModelScope)

    private val savedAlbumsPager = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedAlbumsPagingSource,
    )
    private val savedAlbums = combine(
        savedAlbumsPager.flow.cachedIn(viewModelScope),
        mediaSessionManager.loadedMediaItem,
        mediaSessionManager.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { album ->
            MediaItemUi(
                mediaItem = album,
                isPlaying = isPlaying && album.id == loadedMediaItem?.id
            )
        }
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

    fun onItemClick(mediaItemUi: MediaItemUi) {
        when (mediaItemUi.mediaItem) {
            is Album -> {}
            is Track -> {
                mediaSessionManager.load(mediaItemUi.mediaItem)
                mediaSessionManager.play()
            }
        }
    }

    fun onPlayPauseClick(mediaItemUi: MediaItemUi) {
        if (mediaSessionManager.loadedMediaItem.value?.id != mediaItemUi.mediaItem.id) {
            mediaSessionManager.load(mediaItemUi.mediaItem)
        }

        if (mediaSessionManager.isPlaying.value) {
            mediaSessionManager.pause()
        } else {
            mediaSessionManager.play()
        }
    }
}
