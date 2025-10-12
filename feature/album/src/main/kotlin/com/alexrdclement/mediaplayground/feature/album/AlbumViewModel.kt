package com.alexrdclement.mediaplayground.feature.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.feature.album.navigation.AlbumIdArgKey
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import com.alexrdclement.mediaplayground.ui.model.TrackUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    audioRepository: AudioRepository,
    private val mediaSessionControl: MediaSessionControl,
    private val mediaSessionState: MediaSessionState,
) : ViewModel() {
    private val albumId: AlbumId? = savedStateHandle.get<String>(AlbumIdArgKey)?.let(::AlbumId)

    private val album = MutableStateFlow<Album?>(null)

    private val loadedMediaItem = mediaSessionState.loadedMediaItem
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000L),
            initialValue = null,
        )
    private val isPlaying = mediaSessionState.isPlaying
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000L),
            initialValue = false,
        )

    val uiState = combine(
        album,
        isPlaying,
        loadedMediaItem,
    ) { album, isPlaying, loadedMediaItem ->
        if (album == null) {
            return@combine AlbumUiState.Loading
        }

        val tracks = album.tracks.map { track ->
            TrackUi(
                track = track,
                isLoaded = track.id == loadedMediaItem?.id,
                isPlayable = track.uri != null,
                isPlaying = isPlaying && track.id == loadedMediaItem?.id
            )
        }

        return@combine AlbumUiState.Success(
            imageUrl = album.largeImageUrl,
            title = album.title,
            artists = album.artists,
            tracks = tracks,
            isAlbumPlayable = album.isPlayable,
            isAlbumPlaying = isPlaying && when (val mediaItem = loadedMediaItem) {
                is Album -> mediaItem.id == album.id
                is Track -> mediaItem.simpleAlbum.id == album.id
                null -> false
            },
            isMediaItemLoaded = loadedMediaItem != null,
        )
    }

    init {
        viewModelScope.launch {
            // TODO: error handling
            val albumId = albumId ?: return@launch
            album.value = audioRepository.getAlbum(albumId).guardSuccess { return@launch }
        }
    }

    fun onTrackClick(trackUi: TrackUi) {
        // TODO: error handling
        val album = album.value ?: return
        val simpleTrack = trackUi.track

        viewModelScope.launch {
            val engineControl = mediaSessionControl.getMediaEngineControl()

            if (!isPlayingAlbum()) {
                engineControl.playlistControl.load(album)
            }

            val trackIndex = album.tracks.indexOf(simpleTrack)
            engineControl.playlistControl.seek(trackIndex)

            engineControl.transportControl.play()
        }
    }

    fun onAlbumPlayPauseClick() {
        val album = album.value ?: return
        if (!album.isPlayable) return

        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                if (isPlayingAlbum()) {
                    transportControl.playPause()
                } else {
                    playlistControl.load(album)
                    transportControl.play()
                }
            }
        }
    }

    fun onTrackPlayPauseClick(trackUi: TrackUi) {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                transportControl.playPause()
            }
        }
    }

    private fun isPlayingAlbum(): Boolean {
        val loadedMediaItem = loadedMediaItem.value
        val isPlaying = isPlaying.value
        return isPlaying && when (val mediaItem = loadedMediaItem) {
            is Album -> mediaItem.id == album.value?.id
            is Track -> mediaItem.simpleAlbum.id == album.value?.id
            null -> false
        }
    }
}
