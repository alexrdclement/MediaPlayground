package com.alexrdclement.mediaplayground.feature.audio.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryUiState.ContentReady.LocalContentState
import com.alexrdclement.mediaplayground.feature.audio.library.AudioLibraryUiState.ContentReady.SpotifyContentState
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.mediasession.loadIfNecessary
import com.alexrdclement.mediaplayground.mediasession.playPause
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.shared.model.MediaItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AudioLibraryViewModel @Inject constructor(
    private val spotifyAuth: SpotifyAuth,
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val localAudioRepository: LocalAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
): ViewModel() {
    
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

    private val spotifyContentState: Flow<SpotifyContentState> = spotifyAuth.isLoggedIn
        .map { isLoggedIn ->
            if (!isLoggedIn) {
                return@map SpotifyContentState.NotLoggedIn
            }

            return@map SpotifyContentState.LoggedIn(
                savedTracks = savedTracks,
                savedAlbums = savedAlbums,
            )
        }

    private val localTracksPager = Pager(
        config = pagingConfig,
        pagingSourceFactory = localAudioRepository::getTrackPagingSource,
    )
    private val localTracks = combine(
        localTracksPager.flow.cachedIn(viewModelScope),
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

    private val localContentState: Flow<LocalContentState> = localAudioRepository.getTracks()
        .map { tracks ->
            if (tracks.isEmpty()) {
                LocalContentState.Empty
            } else {
                LocalContentState.Content(
                    tracks = localTracks,
                )
            }
        }

    val uiState: StateFlow<AudioLibraryUiState> = combine(
        spotifyContentState,
        localContentState,
        mediaSessionManager.loadedMediaItem
    ) { spotifyContentState, localContentState, loadedMediaItem ->
        AudioLibraryUiState.ContentReady(
            spotifyContentState = spotifyContentState,
            localContentState = localContentState,
            isMediaItemLoaded = loadedMediaItem != null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AudioLibraryUiState.InitialState
    )

    fun onSpotifyLogOutClick() {
        spotifyAuth.logOut()
    }

    fun onItemClick(mediaItemUi: MediaItemUi) {
        when (val mediaItem = mediaItemUi.mediaItem) {
            is Album -> {}
            is Track -> {
                if (!mediaItem.isPlayable) return
                mediaSessionManager.loadIfNecessary(mediaItemUi.mediaItem)
                mediaSessionManager.play()
            }
        }
    }

    fun onPlayPauseClick(mediaItemUi: MediaItemUi) {
        mediaSessionManager.loadIfNecessary(mediaItemUi.mediaItem)
        mediaSessionManager.playPause()
    }

    fun onMediaImportItemSelected(uri: Uri?) {
        if (uri == null) {
            return
        }
        localAudioRepository.importTrackFromDisk(uri)
    }
}
