package com.alexrdclement.mediaplayground.feature.image.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.alexrdclement.mediaplayground.data.image.ImageRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImageLibraryViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private companion object {
        val pagingConfig = PagingConfig(pageSize = 20)
    }

    private val imagesFlow = imageRepository
        .getImagePagingData(pagingConfig)
        .cachedIn(viewModelScope)

    val uiState: StateFlow<ImageLibraryUiState> = imageRepository
        .getImageCountFlow()
        .map { count ->
            if (count == 0) {
                ImageLibraryUiState.Empty
            } else {
                ImageLibraryUiState.Content(images = imagesFlow)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ImageLibraryUiState.Loading,
        )

    fun onImportItemsSelected(uris: List<Uri>) {
        viewModelScope.launch {
            imageRepository.import(uris)
        }
    }
}
