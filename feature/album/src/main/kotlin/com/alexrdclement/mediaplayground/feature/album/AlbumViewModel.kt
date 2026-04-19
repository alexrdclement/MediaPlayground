package com.alexrdclement.mediaplayground.feature.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.logging.Logger
import com.alexrdclement.logging.error
import com.alexrdclement.mediaplayground.data.album.AudioAlbumRepository
import com.alexrdclement.mediaplayground.media.engine.PlaylistError
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.model.AudioAlbumId
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.largeImageUrl
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.media.session.playlistState
import com.alexrdclement.mediaplayground.ui.model.TrackUi
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@AssistedInject
class AlbumViewModel(
    @Assisted private val albumIdValue: String,
    private val logger: Logger,
    albumRepository: AudioAlbumRepository,
    private val mediaSessionControl: MediaSessionControl,
    private val mediaSessionState: MediaSessionState,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(albumIdValue: String): AlbumViewModel
    }

    private val albumId: AudioAlbumId = AudioAlbumId(albumIdValue)

    private companion object {
        private const val tag = "AlbumViewModel"
        private fun tag(methodName: String) = "$tag#$methodName"
    }

    private val _hasLoadedAlbum = MutableStateFlow(false)

    private val album = albumRepository.getAlbumFlow(albumId)
        .onEach { if (it != null) _hasLoadedAlbum.value = true }
        .stateIn(
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
        _hasLoadedAlbum,
    ) { album, isPlaying, loadedMediaItem, hasLoadedAlbum ->
        if (album == null) {
            return@combine if (hasLoadedAlbum) AlbumUiState.NotFound else AlbumUiState.Loading
        }

        val tracks = album.items.map { track ->
            TrackUi(
                track = track,
                isLoaded = track.id == loadedMediaItem?.id,
                isPlayable = track.isPlayable,
                isPlaying = isPlaying && track.id == loadedMediaItem?.id
            )
        }

        return@combine AlbumUiState.Success(
            imageUrl = album.largeImageUrl,
            title = album.title,
            artists = album.artists,
            tracks = tracks,
            isAlbumPlayable = album.isPlayable,
            isAlbumPlaying = isPlaying && when (loadedMediaItem) {
                is AudioAlbum -> loadedMediaItem.id == album.id
                is Track -> loadedMediaItem.simpleAlbum.id == album.id
                is Clip, null -> false
            },
            isMediaItemLoaded = loadedMediaItem != null,
        )
    }

    fun onTrackClick(trackUi: TrackUi) {
        val album = album.value ?: run {
            logger.error { AlbumUiError.AlbumNotFound }
            return
        }
        val track = trackUi.track

        viewModelScope.launch {
            try {
                val engineControl = mediaSessionControl.getMediaEngineControl()

                if (!isAlbumLoaded()) {
                    engineControl.playlistControl.load(album)
                }

                val trackIndex = album.items.indexOfFirst { it.id == track.id }
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

    private suspend fun isAlbumLoaded(): Boolean {
        val album = album.value ?: return false

        val playlistState = mediaSessionState.playlistState.first()
        val playlist = playlistState.getPlaylist().first()
        if (playlist.isEmpty()) return false

        val loadedMediaItem = loadedMediaItem.value ?: return false
        return when (loadedMediaItem) {
            is AudioAlbum -> loadedMediaItem.id == album.id
            is Track -> playlist.all {
                val asTrack = it as? Track ?: return@all false
                asTrack.simpleAlbum.id == album.id
            }
            else -> false
        }
    }
}
