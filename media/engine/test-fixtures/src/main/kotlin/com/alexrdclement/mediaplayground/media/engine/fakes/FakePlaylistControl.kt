package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaylistControl
import com.alexrdclement.mediaplayground.media.model.audio.MediaItem
import com.alexrdclement.mediaplayground.media.model.audio.MediaItemId
import kotlinx.coroutines.flow.first

class FakePlaylistControl(
    override val playlistState: FakePlaylistState = FakePlaylistState(),
) : PlaylistControl {
    override suspend fun load(mediaItem: MediaItem) {
        playlistState.clear()
        playlistState.setPlaylist(listOf(mediaItem))
        playlistState.setLoadedMediaItemId(mediaItem.id)
    }

    override suspend fun seek(playlistItemIndex: Int) {
        playlistState.seek(playlistItemIndex)
    }

    override suspend fun delete(mediaItemId: MediaItemId) {
        val playlist = playlistState.getPlaylist().first().filter { it.id != mediaItemId }
        playlistState.setPlaylist(playlist)
    }
}
