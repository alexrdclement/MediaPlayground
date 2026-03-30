package com.alexrdclement.mediaplayground.feature.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.image.ImageRepository
import com.alexrdclement.mediaplayground.media.model.audio.Image
import com.alexrdclement.mediaplayground.media.model.audio.ImageId
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class ImageMetadataUiState {
    data object Loading : ImageMetadataUiState()
    data class Loaded(val image: Image) : ImageMetadataUiState()
    data object Error : ImageMetadataUiState()
}

@AssistedInject
class ImageMetadataViewModel(
    @Assisted private val imageIdValue: String,
    imageRepository: ImageRepository,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(imageIdValue: String): ImageMetadataViewModel
    }

    private val imageId = ImageId(imageIdValue)

    val uiState: StateFlow<ImageMetadataUiState> = imageRepository.getImageFlow(imageId)
        .map { image ->
            if (image == null) ImageMetadataUiState.Error
            else ImageMetadataUiState.Loaded(image = image)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ImageMetadataUiState.Loading,
        )
}
