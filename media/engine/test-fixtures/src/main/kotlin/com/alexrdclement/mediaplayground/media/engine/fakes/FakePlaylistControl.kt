package com.alexrdclement.mediaplayground.media.engine.fakes

import com.alexrdclement.mediaplayground.media.engine.PlaylistControl
import javax.inject.Inject
import com.alexrdclement.mediaplayground.media.model.audio.MediaItem

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
