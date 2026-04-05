package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.media.engine.mapper.toMediaItem
import com.alexrdclement.mediaplayground.media.model.Album
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.MediaItemId
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.mapper.toSimpleAlbum
import com.alexrdclement.mediaplayground.media.model.mapper.toTrack
import dev.zacsweers.metro.Inject

class PlaylistControlImpl @Inject constructor(
    override val playlistState: PlaylistState,
    private val mediaControllerHolder: MediaControllerHolder,
): PlaylistControl {

    override suspend fun load(mediaItem: MediaItem) {
        return when (mediaItem) {
            is Album -> loadAlbum(mediaItem)
            is Track -> loadTrack(mediaItem)
        }
    }

    override suspend fun delete(mediaItemId: MediaItemId) {
        val mediaController = mediaControllerHolder.getMediaController()
        val index = (0 until mediaController.mediaItemCount)
            .firstOrNull { i -> mediaController.getMediaItemAt(i).mediaId == mediaItemId.value }
            ?: return
        mediaController.removeMediaItem(index)
    }

    override suspend fun seek(playlistItemIndex: Int) {
        val mediaController = mediaControllerHolder.getMediaController()
        if (playlistItemIndex >= mediaController.mediaItemCount) {
            throw PlaylistError.IndexOutOfBounds(
                index = playlistItemIndex,
                size = mediaController.mediaItemCount,
            )
        }
        mediaController.seekTo(playlistItemIndex, 0)
    }

    private suspend fun loadTrack(track: Track) {
        with(mediaControllerHolder.getMediaController()) {
            val wasPlaying = isPlaying
            pause()

            clearMediaItems()
            setMediaItem(track.toMediaItem())
            if (wasPlaying) play() else pause()
        }
    }

    private suspend fun loadAlbum(album: Album) {
        with(mediaControllerHolder.getMediaController()) {
            val wasPlaying = isPlaying
            pause()

            clearMediaItems()

            val mediaItems = album.tracks.map { simpleTrack ->
                val track = simpleTrack.toTrack(
                    simpleAlbum = album.toSimpleAlbum()
                )
                track.toMediaItem()
            }
            addMediaItems(0, mediaItems)

            if (wasPlaying) play() else pause()
        }
    }
}
