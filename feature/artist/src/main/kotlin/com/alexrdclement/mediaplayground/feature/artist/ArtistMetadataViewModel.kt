package com.alexrdclement.mediaplayground.feature.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.artist.ArtistRepository
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ArtistMetadataUiState {
    data object Loading : ArtistMetadataUiState()
    data class Loaded(
        val artist: Artist,
        val isSaving: Boolean = false,
        val isMediaItemLoaded: Boolean = false,
    ) : ArtistMetadataUiState()
    data object Error : ArtistMetadataUiState()
}

@AssistedInject
class ArtistMetadataViewModel(
    @Assisted private val artistIdValue: String,
    private val artistRepository: ArtistRepository,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(artistIdValue: String): ArtistMetadataViewModel
    }

    private val artistId = ArtistId(artistIdValue)

    val savedEvent = UiEventState<Unit?>()
    val deletedEvent = UiEventState<Unit?>()

    private val _isSaving = MutableStateFlow(false)
    private var hasLoaded = false

    val uiState: StateFlow<ArtistMetadataUiState> = combine(
        artistRepository.getArtistFlow(artistId),
        _isSaving,
        mediaSessionState.loadedMediaItem,
    ) { artist, isSaving, loadedMediaItem ->
        if (artist == null) ArtistMetadataUiState.Error
        else ArtistMetadataUiState.Loaded(
            artist = artist,
            isSaving = isSaving,
            isMediaItemLoaded = loadedMediaItem != null,
        )
    }.onEach { state ->
        if (state is ArtistMetadataUiState.Loaded) hasLoaded = true
        else if (state is ArtistMetadataUiState.Error && hasLoaded) deletedEvent.fire()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ArtistMetadataUiState.Loading,
    )

    fun onSaveClick(
        name: String,
        notes: String?,
    ) {
        if (_isSaving.value) return
        _isSaving.value = true
        viewModelScope.launch {
            try {
                artistRepository.updateArtistName(id = artistId, name = name.ifBlank { null })
                artistRepository.updateArtistNotes(id = artistId, notes = notes)
                savedEvent.fire()
            } finally {
                _isSaving.value = false
            }
        }
    }
}
