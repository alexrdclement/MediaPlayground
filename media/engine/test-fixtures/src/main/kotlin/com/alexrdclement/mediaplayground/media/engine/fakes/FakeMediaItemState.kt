package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaItemState
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakeMediaItemState @Inject constructor() : MediaItemState {

    val mutableLoadedMediaItem = MutableSharedFlow<MediaItem?>()

    override fun getLoadedMediaItem(): Flow<MediaItem?> {
        return mutableLoadedMediaItem
    }
}
