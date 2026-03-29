package com.alexrdclement.mediaplayground.feature.image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.data.image.ImageRepository
import com.alexrdclement.mediaplayground.media.model.audio.AlbumId
import com.alexrdclement.mediaplayground.media.model.audio.Image
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
    @Assisted val albumIdValue: String,
    @Assisted val imageIndex: Int,
    imageRepository: ImageRepository,
) : ViewModel() {

    @AssistedFactory
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(albumIdValue: String, imageIndex: Int): ImageMetadataViewModel
    }

    private val albumId = AlbumId(albumIdValue)

    val uiState: StateFlow<ImageMetadataUiState> = imageRepository.getImagesForAlbumFlow(albumId)
        .map { images ->
            val image = images.getOrNull(imageIndex)
            if (image == null) ImageMetadataUiState.Error
            else ImageMetadataUiState.Loaded(image = image)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ImageMetadataUiState.Loading,
        )
}
