package com.alexrdclement.mediaplayground.feature.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.image.ImageRepository
import com.alexrdclement.mediaplayground.media.model.image.Image
import com.alexrdclement.mediaplayground.media.model.image.ImageId
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

sealed class ImageMetadataUiState {
    data object Loading : ImageMetadataUiState()
    data class Loaded(
        val image: Image,
        val isSaving: Boolean = false,
        val isMediaItemLoaded: Boolean = false,
    ) : ImageMetadataUiState()
    data object Error : ImageMetadataUiState()
}

@AssistedInject
class ImageMetadataViewModel(
    @Assisted private val imageIdValue: String,
    private val imageRepository: ImageRepository,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(imageIdValue: String): ImageMetadataViewModel
    }

    private val imageId = ImageId(imageIdValue)

    val savedEvent = UiEventState<Unit?>()
    private val _isSaving = MutableStateFlow(false)

    val uiState: StateFlow<ImageMetadataUiState> = combine(
        imageRepository.getImageFlow(imageId),
        _isSaving,
        mediaSessionState.loadedMediaItem,
    ) { image, isSaving, loadedMediaItem ->
        if (image == null) ImageMetadataUiState.Error
        else ImageMetadataUiState.Loaded(
            image = image,
            isSaving = isSaving,
            isMediaItemLoaded = loadedMediaItem != null,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ImageMetadataUiState.Loading,
    )

    fun onSaveClick(
        notes: String?,
    ) {
        if (_isSaving.value) return
        _isSaving.value = true
        viewModelScope.launch {
            try {
                imageRepository.updateImageNotes(imageId, notes)
                savedEvent.fire()
            } finally {
                _isSaving.value = false
            }
        }
    }
}
