package com.alexrdclement.mediaplayground.feature.audio.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentState
import com.alexrdclement.mediaplayground.feature.audio.library.content.spotify.SpotifyContentState
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.mediasession.loadIfNecessary
import com.alexrdclement.mediaplayground.mediasession.playPause
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.ui.shared.model.MediaItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AudioLibraryViewModel @Inject constructor(
    localContentState: LocalContentState,
    spotifyContentState: SpotifyContentState,
    private val spotifyAuth: SpotifyAuth,
    private val localAudioRepository: LocalAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
): ViewModel() {
    
    private companion object {
        val pagingConfig = PagingConfig(pageSize = 10)
    }

    val uiState: StateFlow<AudioLibraryUiState> = combine(
        localContentState.flow(viewModelScope, pagingConfig),
        spotifyContentState.flow(viewModelScope, pagingConfig),
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

    fun onMediaImportItemSelected(uri: Uri?) {
        if (uri == null) {
            return
        }
        localAudioRepository.importTrackFromDisk(uri)
    }
}
