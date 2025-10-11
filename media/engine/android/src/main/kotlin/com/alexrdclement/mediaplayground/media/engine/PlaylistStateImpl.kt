package com.alexrdclement.mediaplayground.media.engine

import androidx.media3.common.Player
import com.alexrdclement.mediaplayground.data.audio.AudioRepository
import com.alexrdclement.mediaplayground.model.audio.MediaItem
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.guardSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaylistStateImpl @Inject constructor(
    private val mediaControllerHolder: MediaControllerHolder,
    private val audioRepository: AudioRepository,
): PlaylistState {

    override fun getLoadedMediaItem(): Flow<MediaItem?> {
        return callbackFlow {
            val mediaController = mediaControllerHolder.getMediaController()

            var mediaTransitionJob: Job? = null

            val currentMediaItem = getCurrentMediaItem()
            send(currentMediaItem)

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
                        send(getMediaItem(mediaItem.mediaId))
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

    private suspend fun getCurrentMediaItem(): MediaItem? {
        val mediaController = mediaControllerHolder.getMediaController()
        val mediaId = mediaController.currentMediaItem?.mediaId ?: return null
        return getMediaItem(mediaId = mediaId)
    }

    private suspend fun getMediaItem(mediaId: String): MediaItem? {
        return audioRepository
            .getTrack(id = TrackId(mediaId))
            .guardSuccess {
                return null
            }
    }
}
