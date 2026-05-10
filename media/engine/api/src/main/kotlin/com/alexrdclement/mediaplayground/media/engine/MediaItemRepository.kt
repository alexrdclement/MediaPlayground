package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.media.model.AudioItem
import kotlinx.coroutines.flow.Flow

interface MediaItemRepository {
    fun getMediaItemFlow(id: String): Flow<AudioItem?>
}
