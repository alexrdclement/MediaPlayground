package com.alexrdclement.mediaplayground.feature.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.track.TrackRepository
import com.alexrdclement.mediaplayground.media.model.audio.Track
import com.alexrdclement.mediaplayground.media.model.audio.TrackId
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

sealed class TrackMetadataUiState {
    data object Loading : TrackMetadataUiState()
    data class Loaded(
        val track: Track,
        val isSaving: Boolean = false,
        val isMediaItemLoaded: Boolean = false,
    ) : TrackMetadataUiState()
    data object Error : TrackMetadataUiState()
}

@AssistedInject
class TrackMetadataViewModel(
    @Assisted private val trackIdValue: String,
    private val trackRepository: TrackRepository,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(trackIdValue: String): TrackMetadataViewModel
    }

    private val trackId = TrackId(trackIdValue)

    val savedEvent = UiEventState<Unit?>()
    private val _isSaving = MutableStateFlow(false)

    val uiState: StateFlow<TrackMetadataUiState> = combine(
        trackRepository.getTrackFlow(trackId),
        _isSaving,
        mediaSessionState.loadedMediaItem,
    ) { track, isSaving, loadedMediaItem ->
        if (track == null) TrackMetadataUiState.Error
        else TrackMetadataUiState.Loaded(
            track = track,
            isSaving = isSaving,
            isMediaItemLoaded = loadedMediaItem != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TrackMetadataUiState.Loading,
    )

    fun onSaveClick(
        title: String,
        trackNumber: Int?,
        notes: String?,
    ) {
        if (_isSaving.value) return
        _isSaving.value = true
        viewModelScope.launch {
            try {
                trackRepository.updateTrackTitle(trackId, title)
                trackRepository.updateTrackNumber(trackId, trackNumber)
                trackRepository.updateTrackNotes(trackId, notes)
                savedEvent.fire()
            } finally {
                _isSaving.value = false
            }
        }
    }
}
