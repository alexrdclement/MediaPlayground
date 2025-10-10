package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem

interface MediaItemControl {
    suspend fun load(mediaItem: MediaItem)
    suspend fun loadFromPlaylist(index: Int)
}
