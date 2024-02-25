package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.paging.PagingData
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi
import kotlinx.coroutines.flow.Flow

sealed class LocalContentState {
    data object Empty : LocalContentState()
    data class Content(
        val tracks: Flow<PagingData<MediaItemUi>>,
    ) : LocalContentState()
}
