package com.alexrdclement.mediaplayground.feature.album

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.feature.album.navigation.AlbumIdArgKey
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.mediasession.mapper.toMediaItem
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    audioRepository: AudioRepository,
    private val mediaSessionManager: MediaSessionManager,
) : ViewModel() {
    private val albumId = savedStateHandle.get<String>(AlbumIdArgKey)

    private val _album = MutableStateFlow<Album?>(null)
    val album = _album.asStateFlow()

    init {
        viewModelScope.launch {
            // TODO: error handling
            val albumId = albumId ?: return@launch
            _album.value = audioRepository.getAlbum(albumId)
        }
    }

    fun onPlayTrack(simpleTrack: SimpleTrack) {
        // TODO: error handling
        val album = album.value ?: return

        if (mediaSessionManager.loadedMediaItem.value?.id == albumId) {
            val trackIndex = album.tracks.indexOf(simpleTrack)
            mediaSessionManager.loadFromPlaylist(trackIndex)
        } else {
            val track = simpleTrack.toTrack(
                artists = album.artists,
                simpleAlbum = album.toSimpleAlbum(),
            )
            mediaSessionManager.load(track)
        }
        
        mediaSessionManager.play()
    }
}
