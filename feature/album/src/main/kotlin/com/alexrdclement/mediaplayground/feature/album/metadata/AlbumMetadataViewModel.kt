package com.alexrdclement.mediaplayground.feature.album.metadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.album.AlbumRepository
import com.alexrdclement.mediaplayground.media.model.audio.Album
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.uievent.UiEventState
import com.alexrdclement.uievent.fire
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AlbumMetadataUiState {
    data object Loading : AlbumMetadataUiState()
    data class Loaded(
        val album: Album,
        val isSaving: Boolean = false,
        val isMediaItemLoaded: Boolean = false,
    ) : AlbumMetadataUiState()
    data object Error : AlbumMetadataUiState()
}

@AssistedInject
class AlbumMetadataViewModel(
    @Assisted private val albumIdValue: String,
    private val albumRepository: AlbumRepository,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(albumIdValue: String): AlbumMetadataViewModel
    }

    private val albumId = AlbumId(albumIdValue)

    val savedEvent = UiEventState<Unit?>()

    private val _isSaving = MutableStateFlow(false)

    val uiState: StateFlow<AlbumMetadataUiState> = combine(
        albumRepository.getAlbumFlow(albumId),
        _isSaving,
        mediaSessionState.loadedMediaItem,
    ) { album, isSaving, loadedMediaItem ->
        if (album == null) AlbumMetadataUiState.Error
        else AlbumMetadataUiState.Loaded(
            album = album,
            isSaving = isSaving,
            isMediaItemLoaded = loadedMediaItem != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AlbumMetadataUiState.Loading,
    )

    fun onSaveClick(
        title: String,
        notes: String?,
    ) {
        if (_isSaving.value) return
        _isSaving.value = true
        viewModelScope.launch {
            try {
                albumRepository.updateAlbumTitle(albumId, title)
                albumRepository.updateAlbumNotes(albumId, notes)
                savedEvent.fire()
            } finally {
                _isSaving.value = false
            }
        }
    }
}
