package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaylistState
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.MediaItemId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class FakePlaylistState @Inject constructor() : PlaylistState {

    val mutablePlaylist = MutableSharedFlow<List<MediaItem>>(replay = 1)
    val mutableLoadedMediaItemId = MutableSharedFlow<MediaItemId?>()

    suspend fun clear() {
        mutablePlaylist.emit(emptyList())
        mutableLoadedMediaItemId.emit(null)
    }

    suspend fun setPlaylist(playlist: List<MediaItem>) {
        mutablePlaylist.emit(playlist)
    }

    suspend fun setLoadedMediaItemId(mediaItemId: MediaItemId?) {
        mutableLoadedMediaItemId.emit(mediaItemId)
    }

    suspend fun seek(playlistItemIndex: Int) {
        val playlist = mutablePlaylist.replayCache.firstOrNull() ?: return
        if (playlistItemIndex < 0 || playlistItemIndex >= playlist.size) {
            return
        }
        val mediaItem = playlist[playlistItemIndex]
        mutableLoadedMediaItemId.emit(mediaItem.id)
    }

    override fun getLoadedMediaItemId(): Flow<MediaItemId?> {
        return mutableLoadedMediaItemId
    }

    override fun getPlaylist(): Flow<List<MediaItem>> {
        return mutablePlaylist
    }
}
