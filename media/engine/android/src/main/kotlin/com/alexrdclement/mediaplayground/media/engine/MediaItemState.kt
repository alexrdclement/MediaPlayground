package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaItemState {
    fun getLoadedMediaItem(): Flow<MediaItem?>
}
