package com.alexrdclement.mediaplayground.feature.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.feature.album.navigation.AlbumIdArgKey
import com.alexrdclement.mediaplayground.media.engine.PlaylistError
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl
import com.alexrdclement.mediaplayground.model.result.successOrDefault
import com.alexrdclement.mediaplayground.ui.model.TrackUi
import com.alexrdclement.uiplayground.log.Logger
import com.alexrdclement.uiplayground.log.error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val logger: Logger,
    audioRepository: AudioRepository,
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    private companion object {
        private const val tag = "AlbumViewModel"
        private fun tag(methodName: String) = "$tag#$methodName"
    }

    private val albumId: AlbumId? = savedStateHandle.get<String>(AlbumIdArgKey)?.let(::AlbumId)

    private val album = flow {
        val albumId = albumId ?: return@flow emit(null)
        emit(audioRepository.getAlbum(albumId).successOrDefault(null))
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000L),
        initialValue = null,
    )

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

    fun onTrackClick(trackUi: TrackUi) {
        val album = album.value ?: run {
            logger.error { AlbumUiError.AlbumNotFound }
            return
        }
        val simpleTrack = trackUi.track

        viewModelScope.launch {
            try {
                val engineControl = mediaSessionControl.getMediaEngineControl()

                if (!isAlbumLoaded()) {
                    engineControl.playlistControl.load(album)
                }

                val trackIndex = album.tracks.indexOf(simpleTrack)
                engineControl.playlistControl.seek(trackIndex)

                engineControl.transportControl.play()
            } catch (e: PlaylistError) {
                logger.error(tag = tag("onTrackClick")) {
                    AlbumUiError.PlaylistError(e)
                }
            }
        }
    }

    fun onAlbumPlayPauseClick() {
        val album = album.value ?: run {
            logger.error { AlbumUiError.AlbumNotFound }
            return
        }
        if (!album.isPlayable) {
            logger.error { AlbumUiError.AlbumNotPlayable }
            return
        }

        viewModelScope.launch {
            try {
                with(mediaSessionControl.getMediaEngineControl()) {
                    if (isAlbumLoaded()) {
                        transportControl.playPause()
                    } else {
                        playlistControl.load(album)
                        transportControl.play()
                    }
                }
            } catch (e: PlaylistError) {
                logger.error(tag = tag("onAlbumPlayPauseClick")) {
                    AlbumUiError.PlaylistError(e)
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

    private fun isAlbumLoaded(): Boolean {
        val loadedMediaItem = loadedMediaItem.value
        return when (val mediaItem = loadedMediaItem) {
            is Album -> mediaItem.id == album.value?.id
            is Track -> mediaItem.simpleAlbum.id == album.value?.id
            null -> false
        }
    }
}
