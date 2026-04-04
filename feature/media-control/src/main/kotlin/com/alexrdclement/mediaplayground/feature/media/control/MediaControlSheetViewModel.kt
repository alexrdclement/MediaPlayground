package com.alexrdclement.mediaplayground.feature.media.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.logging.Logger
import com.alexrdclement.logging.error
import com.alexrdclement.mediaplayground.media.engine.PlaybackRateState
import com.alexrdclement.mediaplayground.media.engine.PlayheadState
import com.alexrdclement.mediaplayground.media.engine.PlaylistError
import com.alexrdclement.mediaplayground.media.engine.TimelineState
import com.alexrdclement.mediaplayground.media.engine.TransportState
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.engine.seekIfNecessary
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.media.session.playbackRateState
import com.alexrdclement.mediaplayground.media.session.playheadState
import com.alexrdclement.mediaplayground.media.session.playlistState
import com.alexrdclement.mediaplayground.media.session.timelineState
import com.alexrdclement.mediaplayground.media.session.transportState
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import dev.zacsweers.metro.Inject
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MediaControlSheetViewModel @Inject constructor(
    private val logger: Logger,
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    private companion object {
        private const val tag = "MediaControlSheetViewModel"
        private const val onAlbumPlayPauseClickTag = "$tag#onAlbumPlayPauseClick"
    }

    val transportState = mediaSessionState.transportState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TransportState.Stopped)

    private val isPlaying = transportState
        .map { it == TransportState.Playing }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val playheadState: StateFlow<PlayheadState?> = mediaSessionState.playheadState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val timelineState = mediaSessionState.timelineState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TimelineState(duration = null))

    val playbackRateState = mediaSessionState.playbackRateState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlaybackRateState())

    val loadedMediaItem = mediaSessionState.loadedMediaItem
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null,
        )

    val playlist: StateFlow<PersistentList<MediaItemUi>> = combine(
        mediaSessionState.playlistState.flatMapLatest { it.getPlaylist() },
        isPlaying,
        loadedMediaItem,
    ) { playlist, isPlaying, loadedMediaItem ->
        playlist.map {
            MediaItemUi.from(
                mediaItem = it,
                loadedMediaItem = loadedMediaItem,
                isPlaying = isPlaying,
            )
        }.toPersistentList()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            persistentListOf(),
        )

    val loadedMediaItemIndex: Int?
        get() {
            return playlist.value
                .indexOfFirst { it.mediaItem.id == loadedMediaItem.value?.id }
                .takeIf { it >= 0 }
        }

    fun onPlayPauseClick() {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                transportControl.playPause()
            }
        }
    }

    fun onSkipBackClick() {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                val loadedMediaItemIndex = loadedMediaItemIndex ?: return@launch
                val playheadPosition = playheadControl.getPlayheadPosition()
                when {
                    loadedMediaItemIndex == 0 -> playheadControl.seek(Duration.ZERO)
                    playheadPosition > 5.seconds -> playheadControl.seek(Duration.ZERO)
                    else -> playlistControl.seekIfNecessary(loadedMediaItemIndex - 1)
                }
            }
        }
    }

    fun onSkipClick() {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                val loadedMediaItemIndex = loadedMediaItemIndex ?: return@launch
                val nextMediaItemIndex = (loadedMediaItemIndex + 1) % playlist.value.size
                playlistControl.seekIfNecessary(nextMediaItemIndex)
            }
        }
    }

    fun onSeek(position: Duration) {
        viewModelScope.launch {
            mediaSessionControl.getMediaEngineControl().playheadControl.seek(position)
        }
    }

    fun onItemClick(item: MediaItemUi) {
        viewModelScope.launch {
            try {
                with(mediaSessionControl.getMediaEngineControl()) {
                    playlistControl.seek(getItemIndex(item))
                    if (!isPlaying.value) {
                        transportControl.play()
                    }
                }
            } catch (e: PlaylistError) {
                logger.error(tag = onAlbumPlayPauseClickTag) {
                    MediaControlSheetError.PlaylistError(e)
                }
            }
        }
    }

    fun onItemPlayPauseClick(item: MediaItemUi) {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                if (loadedMediaItem.value == item.mediaItem && isPlaying.value) {
                    transportControl.pause()
                } else {
                    playlistControl.seekIfNecessary(getItemIndex(item))
                    transportControl.play()
                }
            }
        }
    }

    private fun getItemIndex(item: MediaItemUi): Int {
        return playlist.value.indexOf(item)
    }
}
