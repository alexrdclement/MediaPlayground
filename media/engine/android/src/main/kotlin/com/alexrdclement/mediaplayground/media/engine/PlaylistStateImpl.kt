package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.MediaItemId
import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistStateImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
    private val audioRepository: AudioRepository,
): PlaylistState {

    override fun getLoadedMediaItemId(): Flow<MediaItemId?> {
        return callbackFlow {
            val mediaController = mediaControllerHolder.getMediaController()

            var mediaTransitionJob: Job? = null

            val currentMediaItem = getCurrentMediaItem()
            send(currentMediaItem?.id)

            val listener = object : Player.Listener {
                override fun onMediaItemTransition(
                    mediaItem: androidx.media3.common.MediaItem?,
                    reason: Int
                ) {
                    if (mediaItem == null) {
                        trySend(null)
                        return
                    }

                    mediaTransitionJob = launch {
                        val item = getMediaItem(mediaItem.mediaId)
                        trySend(item?.id)
                    }
                }
            }
            mediaController.addListener(listener)

            awaitClose {
                mediaController.removeListener(listener)
                mediaTransitionJob?.cancel()
            }
        }
    }

    override fun getPlaylist(): Flow<List<MediaItem>> {
        return callbackFlow {
            val mediaController = mediaControllerHolder.getMediaController()

            val currentPlaylist = mediaController.currentTimeline.getMediaItems()
            send(currentPlaylist)

            val listener = object : Player.Listener {
                override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                    launch {
                        val updatedPlaylist = timeline.getMediaItems()
                        trySend(updatedPlaylist)
                    }
                }
            }
            mediaController.addListener(listener)

            awaitClose {
                mediaController.removeListener(listener)
            }
        }
    }

    private suspend fun getCurrentMediaItem(): MediaItem? {
        val mediaController = mediaControllerHolder.getMediaController()
        val mediaId = mediaController.currentMediaItem?.mediaId ?: return null
        return getMediaItem(mediaId = mediaId)
    }

    private suspend fun Timeline.getMediaItems(): List<MediaItem> {
        return coroutineScope {
            val jobs = (0 until windowCount).mapNotNull { index ->
                getWindow(index).mediaItem
            }.mapNotNull { mediaItem ->
                async {
                    getMediaItem(mediaItem.mediaId)
                }
            }
            jobs
                .awaitAll()
                .mapNotNull { it }
        }
    }

    private fun Timeline.getWindow(index: Int): Timeline.Window {
        return Timeline.Window().apply { getWindow(index, this) }
    }

    private suspend fun getMediaItem(mediaId: String): MediaItem? {
        return audioRepository
            .getTrack(id = TrackId(mediaId), source = Source.Local)
            .guardSuccess {
                return null
            }
    }
}
