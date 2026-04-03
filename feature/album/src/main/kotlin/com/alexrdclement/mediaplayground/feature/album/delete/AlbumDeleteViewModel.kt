package com.alexrdclement.mediaplayground.feature.album.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.album.AlbumRepository
import com.alexrdclement.mediaplayground.media.engine.deleteIfNecessary
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class DeleteState {
    data object Confirming : DeleteState()
    data object Deleting : DeleteState()
    data object Deleted : DeleteState()
}

@AssistedInject
class AlbumDeleteViewModel(
    @Assisted val idValue: String,
    private val albumRepository: AlbumRepository,
    private val mediaSessionControl: MediaSessionControl,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(idValue: String): AlbumDeleteViewModel
    }

    private val _deleteState = MutableStateFlow<DeleteState>(DeleteState.Confirming)
    val deleteState: StateFlow<DeleteState> = _deleteState.asStateFlow()

    fun onDeleteConfirmed() {
        viewModelScope.launch {
            _deleteState.update { DeleteState.Deleting }
            val albumId = AlbumId(idValue)
            val playlistControl = mediaSessionControl.getMediaEngineControl().playlistControl
            albumRepository.getAlbum(albumId)?.tracks?.forEach { track ->
                playlistControl.deleteIfNecessary(track.id)
            }
            albumRepository.deleteAlbum(albumId)
            _deleteState.update { DeleteState.Deleted }
        }
    }
}
