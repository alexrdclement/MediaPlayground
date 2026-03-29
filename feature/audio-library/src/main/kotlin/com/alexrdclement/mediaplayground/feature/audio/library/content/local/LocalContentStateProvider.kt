package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.album.AlbumRepository
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackRepository
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import dev.zacsweers.metro.Inject

class LocalContentStateProvider @Inject constructor(
    private val localTrackRepository: LocalTrackRepository,
    private val albumRepository: AlbumRepository,
    private val mediaSessionState: MediaSessionState,
) {

    fun flow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ): Flow<LocalContentState> {
        val tracksFlow = tracksFlow(coroutineScope, pagingConfig)
        val albumsFlow = albumsFlow(coroutineScope, pagingConfig)
        return combine(
            localTrackRepository.getTrackCountFlow(),
            albumRepository.getAlbumCountFlow(),
        ) { trackCount, albumCount ->
            if (trackCount == 0 && albumCount == 0) {
                LocalContentState.Empty
            } else {
                LocalContentState.Content(
                    tracks = tracksFlow,
                    albums = albumsFlow,
                )
            }
        }.distinctUntilChanged()
    }

    private fun tracksFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ) = combine(
        localTrackRepository.getTrackPagingData(pagingConfig).cachedIn(coroutineScope),
        mediaSessionState.loadedMediaItem,
        mediaSessionState.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { track ->
            MediaItemUi.from(
                mediaItem = track,
                loadedMediaItem = loadedMediaItem,
                isPlaying = isPlaying,
            )
        }
    }.cachedIn(coroutineScope)

    private fun albumsFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ) = combine(
        albumRepository.getAlbumPagingData(pagingConfig).cachedIn(coroutineScope),
        mediaSessionState.loadedMediaItem,
        mediaSessionState.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { album ->
            MediaItemUi.from(
                mediaItem = album,
                loadedMediaItem = loadedMediaItem,
                isPlaying = isPlaying,
            )
        }
    }.cachedIn(coroutineScope)
}
