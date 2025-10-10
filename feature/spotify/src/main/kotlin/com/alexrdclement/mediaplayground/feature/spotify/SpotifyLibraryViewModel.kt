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
import com.alexrdclement.mediaplayground.media.engine.loadIfNecessary
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyLibraryViewModel @Inject constructor(
    private val spotifyAuth: SpotifyAuth,
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
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
        mediaSessionState.loadedMediaItem,
        mediaSessionState.isPlaying,
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
        mediaSessionState.loadedMediaItem,
        mediaSessionState.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { album ->
            MediaItemUi(
                mediaItem = album,
                isPlaying = isPlaying && album.id == loadedMediaItem?.id
            )
        }
    }.cachedIn(viewModelScope)

    val uiState: StateFlow<SpotifyLibraryUiState> = spotifyAuth.isLoggedIn.combine(
        mediaSessionState.loadedMediaItem,
    ) { isLoggedIn, loadedMediaItem ->
        if (!isLoggedIn) {
            return@combine SpotifyLibraryUiState.NotLoggedIn
        }

        return@combine SpotifyLibraryUiState.LoggedIn(
            savedTracks = savedTracks,
            savedAlbums = savedAlbums,
            isMediaItemLoaded = loadedMediaItem != null,
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
        when (val mediaItem = mediaItemUi.mediaItem) {
            is Album -> {}
            is Track -> {
                if (!mediaItem.isPlayable) return
                viewModelScope.launch {
                    with(mediaSessionControl.getMediaEngineControl()) {
                        loadIfNecessary(mediaItemUi.mediaItem)
                        transportControl.play()
                    }
                }
            }
        }
    }

    fun onPlayPauseClick(mediaItemUi: MediaItemUi) {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                loadIfNecessary(mediaItemUi.mediaItem)
                transportControl.playPause()
            }
        }
    }
}
