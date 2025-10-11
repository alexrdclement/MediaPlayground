package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.model.audio.MediaItem
import kotlinx.coroutines.flow.first

interface PlaylistControl {
    val playlistState: PlaylistState
    suspend fun load(mediaItem: MediaItem)
    suspend fun seek(playlistItemIndex: Int)
}

suspend fun PlaylistControl.loadIfNecessary(mediaItem: MediaItem) {
    playlistState.getLoadedMediaItem().first()?.let { loadedMediaItem ->
        if (mediaItem.id != loadedMediaItem.id) {
            load(mediaItem)
        }
    } ?: load(mediaItem)
}
