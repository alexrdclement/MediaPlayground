package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow

interface PlaylistState {
    fun getLoadedMediaItem(): Flow<MediaItem?>
}
