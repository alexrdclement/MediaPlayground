package com.alexrdclement.mediaplayground.feature.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.feature.album.navigation.AlbumIdArgKey
import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
import com.alexrdclement.mediaplayground.media.session.loadIfNecessary
import com.alexrdclement.mediaplayground.media.session.playPause
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.largeImageUrl
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import com.alexrdclement.mediaplayground.ui.shared.model.TrackUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    audioRepository: AudioRepository,
    private val mediaSessionManager: MediaSessionManager,
) : ViewModel() {
    private val albumId: AlbumId? = savedStateHandle.get<String>(AlbumIdArgKey)?.let(::AlbumId)

    private val album = MutableStateFlow<Album?>(null)

    val uiState = combine(
        album,
        mediaSessionManager.isPlaying,
        mediaSessionManager.loadedMediaItem
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
            isAlbumPlaying = loadedMediaItem?.id == albumId && isPlaying,
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

        if (mediaSessionManager.loadedMediaItem.value?.id == albumId) {
            val trackIndex = album.tracks.indexOf(simpleTrack)
            mediaSessionManager.loadFromPlaylist(trackIndex)
        } else {
            val track = simpleTrack.toTrack(album)
            mediaSessionManager.load(track)
        }

        mediaSessionManager.play()
    }

    fun onAlbumPlayPauseClick() {
        val album = album.value ?: return
        if (!album.isPlayable) return

        mediaSessionManager.loadIfNecessary(album)
        mediaSessionManager.playPause()
    }

    fun onTrackPlayPauseClick(trackUi: TrackUi) {
        mediaSessionManager.playPause()
    }
}
