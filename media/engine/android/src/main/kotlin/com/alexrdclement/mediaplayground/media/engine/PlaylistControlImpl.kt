package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.media.engine.mapper.toMediaItem
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack
import javax.inject.Inject

class PlaylistControlImpl @Inject constructor(
    override val playlistState: PlaylistState,
    private val mediaControllerHolder: MediaControllerHolder,
): PlaylistControl {

    override suspend fun load(mediaItem: MediaItem) {
        when (mediaItem) {
            is Album -> loadAlbum(mediaItem)
            is Track -> loadTrack(mediaItem)
        }
    }

    override suspend fun seek(playlistItemIndex: Int) {
        val mediaController = mediaControllerHolder.getMediaController()
        if (playlistItemIndex >= mediaController.mediaItemCount) {
            // TODO: log error
            return
        }
        mediaController.seekTo(playlistItemIndex, 0)
    }

    private suspend fun loadTrack(track: Track) {
        mediaControllerHolder.getMediaController().setMediaItem(track.toMediaItem())
    }

    private suspend fun loadAlbum(album: Album) {
        val mediaController = mediaControllerHolder.getMediaController()
        mediaController.clearMediaItems()

        val mediaItems = album.tracks.map { simpleTrack ->
            val track = simpleTrack.toTrack(
                simpleAlbum = album.toSimpleAlbum()
            )
            track.toMediaItem()
        }
        mediaController.addMediaItems(0, mediaItems)
    }
}
