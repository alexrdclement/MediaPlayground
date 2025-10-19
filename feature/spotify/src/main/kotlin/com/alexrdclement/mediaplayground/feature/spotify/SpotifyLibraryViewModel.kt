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
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuthState
import com.alexrdclement.mediaplayground.media.engine.PlaylistError
import com.alexrdclement.mediaplayground.media.engine.loadIfNecessary
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.log.Logger
import com.alexrdclement.log.error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyLibraryViewModel @Inject constructor(
    private val logger: Logger,
    private val spotifyAuth: SpotifyAuth,
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    private companion object {
        val pagingConfig = PagingConfig(pageSize = 10)

        private const val tag = "SpotifyLibraryViewModel"
        private const val onItemClickTag = "$tag#onItemClick"
        private const val onPlayPauseClickTag = "$tag#onPlayPauseClick"
    }

    private val loadedMediaItem = mediaSessionState.loadedMediaItem
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = null,
        )
    private val isPlaying = mediaSessionState.isPlaying
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            replay = 1,
        )

    private val savedTracksPager = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedTracksPagingSource,
    )
    private val savedTracks: Flow<PagingData<MediaItemUi>> = combine(
        savedTracksPager.flow.cachedIn(viewModelScope),
        loadedMediaItem,
        isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { track ->
            MediaItemUi.from(
                mediaItem = track,
                loadedMediaItem = loadedMediaItem,
                isPlaying = isPlaying,
            )
        }
    }.cachedIn(viewModelScope)

    private val savedAlbumsPager = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedAlbumsPagingSource,
    )
    private val savedAlbums = combine(
        savedAlbumsPager.flow.cachedIn(viewModelScope),
        loadedMediaItem,
        isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { album ->
            MediaItemUi.from(
                mediaItem = album,
                loadedMediaItem = loadedMediaItem,
                isPlaying = isPlaying,
            )
        }
    }.cachedIn(viewModelScope)

    val uiState: StateFlow<SpotifyLibraryUiState> = spotifyAuth.authState.combine(
        loadedMediaItem,
    ) { authState, loadedMediaItem ->
        when (authState) {
            is SpotifyAuthState.LoggedIn -> SpotifyLibraryUiState.LoggedIn(
                savedTracks = savedTracks,
                savedAlbums = savedAlbums,
                isMediaItemLoaded = loadedMediaItem != null,
            )
            is SpotifyAuthState.LoggingIn,
            is SpotifyAuthState.LoggedOut,
            is SpotifyAuthState.Error,
            -> SpotifyLibraryUiState.NotLoggedIn
        }
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
                if (!mediaItem.isPlayable) {
                    logger.error { SpotifyLibraryUiError.NotPlayable }
                    return
                }
                viewModelScope.launch {
                    try {
                        with(mediaSessionControl.getMediaEngineControl()) {
                            playlistControl.loadIfNecessary(mediaItemUi.mediaItem)
                            transportControl.play()
                        }
                    } catch (e: PlaylistError) {
                        logger.error(onItemClickTag) {
                            SpotifyLibraryUiError.PlaylistError(error = e)
                        }
                    }
                }
            }
        }
    }

    fun onPlayPauseClick(mediaItemUi: MediaItemUi) {
        viewModelScope.launch {
            try {
                with(mediaSessionControl.getMediaEngineControl()) {
                    val loadedMediaItem = loadedMediaItem.value
                    if (mediaItemUi.mediaItem.id == loadedMediaItem?.id) {
                        transportControl.playPause()
                        return@launch
                    }
                    playlistControl.load(mediaItemUi.mediaItem)
                    transportControl.play()
                }
            } catch (e: PlaylistError) {
                logger.error(onPlayPauseClickTag) {
                    SpotifyLibraryUiError.PlaylistError(error = e)
                }
            }
        }
    }
}
