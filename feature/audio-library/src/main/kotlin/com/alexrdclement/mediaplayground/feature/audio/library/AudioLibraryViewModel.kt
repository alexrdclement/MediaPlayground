package com.alexrdclement.mediaplayground.feature.audio.library

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackRepository
import com.alexrdclement.mediaplayground.data.track.local.TrackImportResult
import com.alexrdclement.mediaplayground.data.track.local.TrackImportState
import com.alexrdclement.mediaplayground.feature.audio.library.content.local.LocalContentStateProvider
import com.alexrdclement.mediaplayground.media.engine.PlaylistError
import com.alexrdclement.mediaplayground.media.engine.loadIfNecessary
import com.alexrdclement.mediaplayground.media.engine.playPause
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.media.model.audio.Album
import com.alexrdclement.mediaplayground.media.model.audio.Track
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import com.alexrdclement.logging.Logger
import com.alexrdclement.logging.error
import com.alexrdclement.logging.infoString
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch

class AudioLibraryViewModel @Inject constructor(
    private val logger: Logger,
    localContentStateProvider: LocalContentStateProvider,
    private val localTrackRepository: LocalTrackRepository,
    private val mediaSessionControl: MediaSessionControl,
    mediaSessionState: MediaSessionState,
) : ViewModel() {

    private companion object {
        val pagingConfig = PagingConfig(pageSize = 10)

        private const val tag = "AudioLibraryViewModel"
        private const val onItemClickTag = "$tag#onItemClick"
        private const val onPlayPauseClickTag = "$tag#onPlayPauseClick"
        private const val onMediaImportItemSelectedTag = "$tag#onMediaImportItemSelected"
    }

    private var importStateJob: Job? = null

    private val loadedMediaItem = mediaSessionState.loadedMediaItem
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    val uiState: StateFlow<AudioLibraryUiState> = combine(
        localContentStateProvider.flow(viewModelScope, pagingConfig),
        loadedMediaItem,
    ) { localContentState, loadedMediaItem ->
        AudioLibraryUiState.ContentReady(
            localContentState = localContentState,
            isMediaItemLoaded = loadedMediaItem != null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AudioLibraryUiState.InitialState
    )

    fun onItemClick(mediaItemUi: MediaItemUi) {
        when (val mediaItem = mediaItemUi.mediaItem) {
            is Album -> {}
            is Track -> {
                if (!mediaItem.isPlayable) {
                    logger.error { AudioLibraryUiError.NotPlayable }
                    return
                }
                viewModelScope.launch {
                    try {
                        with(mediaSessionControl.getMediaEngineControl()) {
                            playlistControl.loadIfNecessary(mediaItemUi.mediaItem)
                            transportControl.play()
                        }
                    } catch (e: PlaylistError) {
                        logger.error(onItemClickTag) {
                            AudioLibraryUiError.PlaylistError(error = e)
                        }
                    }
                }
            }
        }
    }

    fun onPlayPauseClick(mediaItemUi: MediaItemUi) {
        viewModelScope.launch {
            try {
                with(mediaSessionControl.getMediaEngineControl()) {
                    val loadedMediaItem = loadedMediaItem.value
                    if (mediaItemUi.mediaItem.id == loadedMediaItem?.id) {
                        transportControl.playPause()
                        return@launch
                    }
                    playlistControl.load(mediaItemUi.mediaItem)
                    transportControl.play()
                }
            } catch (e: PlaylistError) {
                logger.error(onPlayPauseClickTag) {
                    AudioLibraryUiError.PlaylistError(error = e)
                }
            }
        }
    }

    fun onMediaImportItemSelected(uris: List<Uri>) {
        importStateJob?.cancel()
        importStateJob = viewModelScope.launch {
            importTracks(uris = uris)
        }
    }

    private suspend fun importTracks(uris: List<Uri>) {
        val importedUris = mutableSetOf<Uri>()
        localTrackRepository.importTracksFromDisk(uris)
            .map(::mapToResults)
            .takeWhile { resultsByUri ->
                val failuresByUri = mapToFailures(resultsByUri)
                failuresByUri.forEach { (uri, failure) ->
                    logger.error(onMediaImportItemSelectedTag) {
                        AudioLibraryUiError.ImportFailure(uri, failure.error)
                    }
                }
                failuresByUri.isEmpty()
            }
            .map(::mapToSuccess)
            .collect { successByUri ->
                for ((uri, success) in successByUri) {
                    if (importedUris.contains(uri)) continue
                    importedUris.add(uri)

                    logger.infoString(onMediaImportItemSelectedTag) {
                        "Imported track ${success.track.title} from $uri"
                    }
                }
            }
    }

    private fun mapToResults(
        statesByUri: Map<Uri, TrackImportState>,
    ): Map<Uri, TrackImportResult> {
        return statesByUri.mapNotNull { (uri, state) ->
            (state as? TrackImportState.Completed)?.result ?: return@mapNotNull null
            uri to state.result
        }.toMap()
    }

    private fun mapToSuccess(
        resultsByUri: Map<Uri, TrackImportResult>,
    ): Map<Uri, TrackImportResult.Success> = resultsByUri.mapNotNull { (uri, result) ->
        val mappedResult = result as? TrackImportResult.Success ?: return@mapNotNull null
        uri to mappedResult
    }.toMap()

    private fun mapToFailures(
        resultsByUri: Map<Uri, TrackImportResult>,
    ) = resultsByUri.mapNotNull { (uri, result) ->
        val mappedResult = result as? TrackImportResult.Failure ?: return@mapNotNull null
        uri to mappedResult
    }.toMap()
}
