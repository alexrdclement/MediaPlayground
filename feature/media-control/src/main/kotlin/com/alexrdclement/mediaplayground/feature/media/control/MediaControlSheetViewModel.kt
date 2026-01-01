package com.alexrdclement.mediaplayground.feature.media.control

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexrdclement.mediaplayground.media.engine.PlaylistError
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.engine.seekIfNecessary
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.media.session.playlistState
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.logging.Logger
import com.alexrdclement.logging.error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaControlSheetViewModel @Inject constructor(
    private val logger: Logger,
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    private companion object {
        private const val tag = "MediaControlSheetViewModel"
        private const val onAlbumPlayPauseClickTag = "$tag#onAlbumPlayPauseClick"
    }

    val isPlaying = mediaSessionState.isPlaying
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

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

    fun onPlayPauseClick() {
        viewModelScope.launch {
            with(mediaSessionControl.getMediaEngineControl()) {
                transportControl.playPause()
            }
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
