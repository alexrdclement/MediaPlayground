package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import androidx.media3.common.Timeline
import com.alexrdclement.mediaplayground.media.model.MediaItem
import com.alexrdclement.mediaplayground.media.model.MediaItemId
import com.alexrdclement.mediaplayground.media.model.TrackId
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import dev.zacsweers.metro.Inject

class PlaylistStateImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
    private val mediaItemRepository: MediaItemRepository,
): PlaylistState {

    override fun getLoadedMediaItemId(): Flow<MediaItemId?> {
        return callbackFlow {
            val mediaController = mediaControllerHolder.getMediaController()

            val currentMediaItemId = mediaController.currentMediaItem?.mediaId
            send(currentMediaItemId?.let { TrackId(it) })

            val listener = object : Player.Listener {
                override fun onMediaItemTransition(
                    mediaItem: androidx.media3.common.MediaItem?,
                    reason: Int
                ) {
                    trySend(mediaItem?.mediaId?.let { TrackId(it) })
                }
            }
            mediaController.addListener(listener)

            awaitClose {
                mediaController.removeListener(listener)
            }
        }
    }

    override fun getPlaylist(): Flow<List<MediaItem>> {
        val timelineIds: Flow<List<String>> = callbackFlow {
            val mediaController = mediaControllerHolder.getMediaController()
            send(mediaController.currentTimeline.getMediaIds())
            val listener = object : Player.Listener {
                override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                    trySend(timeline.getMediaIds())
                }
            }
            mediaController.addListener(listener)
            awaitClose { mediaController.removeListener(listener) }
        }

        return timelineIds.flatMapLatest { ids ->
            if (ids.isEmpty()) flowOf(emptyList())
            else combine(ids.map { mediaItemRepository.getMediaItemFlow(it) }) { items ->
                items.filterNotNull()
            }
        }
    }

    private fun Timeline.getMediaIds(): List<String> {
        return (0 until windowCount).mapNotNull { index ->
            getWindow(index).mediaItem.mediaId.takeIf { it.isNotEmpty() }
        }
    }

    private fun Timeline.getWindow(index: Int): Timeline.Window {
        return Timeline.Window().apply { getWindow(index, this) }
    }
}
