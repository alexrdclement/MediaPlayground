package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaylistState
import com.alexrdclement.mediaplayground.media.model.AudioItem
import com.alexrdclement.mediaplayground.media.model.AudioItemId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakePlaylistState : PlaylistState {

    val mutablePlaylist = MutableSharedFlow<List<AudioItem>>(replay = 1)
    val mutableLoadedAudioItemId = MutableSharedFlow<AudioItemId?>()

    suspend fun clear() {
        mutablePlaylist.emit(emptyList())
        mutableLoadedAudioItemId.emit(null)
    }

    suspend fun setPlaylist(playlist: List<AudioItem>) {
        mutablePlaylist.emit(playlist)
    }

    suspend fun setLoadedAudioItemId(mediaItemId: AudioItemId?) {
        mutableLoadedAudioItemId.emit(mediaItemId)
    }

    suspend fun seek(playlistItemIndex: Int) {
        val playlist = mutablePlaylist.replayCache.firstOrNull() ?: return
        if (playlistItemIndex < 0 || playlistItemIndex >= playlist.size) {
            return
        }
        val mediaItem = playlist[playlistItemIndex]
        mutableLoadedAudioItemId.emit(mediaItem.id)
    }

    override fun getLoadedAudioItemId(): Flow<AudioItemId?> {
        return mutableLoadedAudioItemId
    }

    override fun getPlaylist(): Flow<List<AudioItem>> {
        return mutablePlaylist
    }
}
