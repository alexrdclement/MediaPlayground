package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.media.model.AudioItem
import com.alexrdclement.mediaplayground.media.model.MediaItemId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface PlaylistState {
    fun getLoadedMediaItemId(): Flow<MediaItemId?>
    fun getPlaylist(): Flow<List<AudioItem>>
}

fun PlaylistState.getLoadedMediaItem(): Flow<AudioItem?> {
    return combine(
        getLoadedMediaItemId(),
        getPlaylist(),
    ) { currentId, playlist ->
        playlist.find { it.id == currentId }
    }
}

fun PlaylistState.getLoadedMediaItemIndex(): Flow<Int?> {
    return combine(
        getLoadedMediaItemId(),
        getPlaylist(),
    ) { currentId, playlist ->
        playlist.indexOfFirst { it.id == currentId }.let { if (it >= 0) it else null }
    }
}
