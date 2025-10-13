package com.alexrdclement.mediaplayground.feature.audio.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentStateProvider
import com.alexrdclement.mediaplayground.feature.audio.library.content.spotify.SpotifyContentStateProvider
import com.alexrdclement.mediaplayground.media.engine.loadIfNecessary
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.uiplayground.log.Logger
import com.alexrdclement.uiplayground.log.error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioLibraryViewModel @Inject constructor(
    private val logger: Logger,
    localContentStateProvider: LocalContentStateProvider,
    spotifyContentStateProvider: SpotifyContentStateProvider,
    private val spotifyAuth: SpotifyAuth,
    private val localAudioRepository: LocalAudioRepository,
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
): ViewModel() {
    
    private companion object {
        val pagingConfig = PagingConfig(pageSize = 10)
    }

    private val loadedMediaItem = mediaSessionState.loadedMediaItem
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val uiState: StateFlow<AudioLibraryUiState> = combine(
        localContentStateProvider.flow(viewModelScope, pagingConfig),
        spotifyContentStateProvider.flow(viewModelScope, pagingConfig),
        loadedMediaItem,
    ) { localContentState, spotifyContentState, loadedMediaItem ->
        AudioLibraryUiState.ContentReady(
            localContentState = localContentState,
            spotifyContentState = spotifyContentState,
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
                if (!mediaItem.isPlayable) {
                    logger.error { AudioLibraryError.NotPlayable }
                    return
                }
                viewModelScope.launch {
                    with(mediaSessionControl.getMediaEngineControl()) {
                        playlistControl.loadIfNecessary(mediaItemUi.mediaItem)
                        transportControl.play()
                    }
                }
            }
        }
    }

    fun onPlayPauseClick(mediaItemUi: MediaItemUi) {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                val loadedMediaItem = loadedMediaItem.value
                if (mediaItemUi.mediaItem.id == loadedMediaItem?.id) {
                    transportControl.playPause()
                    return@launch
                }
                playlistControl.load(mediaItemUi.mediaItem)
                transportControl.play()
            }
        }
    }

    fun onMediaImportItemSelected(uris: List<Uri>) {
        localAudioRepository.importTracksFromDisk(uris)
    }
}
