package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.MediaItemControl
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import javax.inject.Inject

class FakeMediaItemControl @Inject constructor(
    val mediaItemState: FakeMediaItemState = FakeMediaItemState(),
) : MediaItemControl {
    override suspend fun load(mediaItem: MediaItem) {
        mediaItemState.mutableLoadedMediaItem.emit(mediaItem)
    }

    override suspend fun loadFromPlaylist(index: Int) {
        // TODO
    }
}
