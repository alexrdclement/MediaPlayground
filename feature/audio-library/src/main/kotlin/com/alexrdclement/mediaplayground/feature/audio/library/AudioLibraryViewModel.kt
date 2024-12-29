package com.alexrdclement.mediaplayground.feature.audio.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentStateProvider
import com.alexrdclement.mediaplayground.feature.audio.library.content.spotify.SpotifyContentStateProvider
import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
import com.alexrdclement.mediaplayground.media.session.loadIfNecessary
import com.alexrdclement.mediaplayground.media.session.playPause
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AudioLibraryViewModel @Inject constructor(
    localContentStateProvider: LocalContentStateProvider,
    spotifyContentStateProvider: SpotifyContentStateProvider,
    private val spotifyAuth: SpotifyAuth,
    private val localAudioRepository: LocalAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
): ViewModel() {
    
    private companion object {
        val pagingConfig = PagingConfig(pageSize = 10)
    }

    val uiState: StateFlow<AudioLibraryUiState> = combine(
        localContentStateProvider.flow(viewModelScope, pagingConfig),
        spotifyContentStateProvider.flow(viewModelScope, pagingConfig),
        mediaSessionManager.loadedMediaItem
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

    fun onMediaImportItemSelected(uris: List<Uri>) {
        if (uris.isEmpty()) {
            return
        }
        localAudioRepository.importTracksFromDisk(uris)
    }
}
