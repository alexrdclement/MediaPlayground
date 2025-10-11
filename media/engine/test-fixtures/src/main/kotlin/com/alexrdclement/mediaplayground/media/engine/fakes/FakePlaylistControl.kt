package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaylistControl
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import javax.inject.Inject

class FakePlaylistControl @Inject constructor(
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
}
