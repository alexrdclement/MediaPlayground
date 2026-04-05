package com.alexrdclement.mediaplayground.feature.image.library

import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.media.model.Image
import kotlinx.coroutines.flow.Flow

sealed class ImageLibraryUiState {
    data object Loading : ImageLibraryUiState()
    data object Empty : ImageLibraryUiState()
    data class Content(
        val images: Flow<PagingData<Image>>,
    ) : ImageLibraryUiState()
}
