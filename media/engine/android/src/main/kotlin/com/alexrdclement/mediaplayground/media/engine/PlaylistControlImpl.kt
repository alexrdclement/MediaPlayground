package com.alexrdclement.mediaplayground.media.engine

import com.alexrdclement.mediaplayground.media.engine.mapper.toMediaItem
import com.alexrdclement.mediaplayground.media.model.AlbumTrack
import com.alexrdclement.mediaplayground.media.model.AudioAlbum
import com.alexrdclement.mediaplayground.media.model.Clip
import com.alexrdclement.mediaplayground.media.model.MediaAsset
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.MediaItemId
import com.alexrdclement.mediaplayground.media.model.Track
import com.alexrdclement.mediaplayground.media.model.TrackClip
import com.alexrdclement.mediaplayground.media.store.PathProvider
import dev.zacsweers.metro.Inject

class PlaylistControlImpl @Inject constructor(
    override val playlistState: PlaylistState,
    private val mediaControllerHolder: MediaControllerHolder,
    private val pathProvider: PathProvider,
): PlaylistControl {

    override suspend fun load(mediaItem: MediaItem) {
        return when (mediaItem) {
            is AudioAlbum -> loadAlbum(mediaItem)
            is Track -> loadTrack(mediaItem)
            is AlbumTrack -> Unit
            is Clip -> Unit
            is TrackClip<*> -> Unit
            is MediaAsset -> Unit
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
            setMediaItem(track.toMediaItem(pathProvider))
            if (wasPlaying) play() else pause()
        }
    }

    private suspend fun loadAlbum(album: AudioAlbum) {
        with(mediaControllerHolder.getMediaController()) {
            val wasPlaying = isPlaying
            pause()

            clearMediaItems()

            val mediaItems = album.items.map { it.toMediaItem(pathProvider) }
            addMediaItems(0, mediaItems)

            if (wasPlaying) play() else pause()
        }
    }
}
