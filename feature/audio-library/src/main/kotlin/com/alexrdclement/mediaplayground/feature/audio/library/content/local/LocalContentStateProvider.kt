package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class LocalContentStateProvider @Inject constructor(
    private val localAudioRepository: LocalAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
) {

    fun flow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ): Flow<LocalContentState> {
        val tracksFlow = tracksFlow(coroutineScope, pagingConfig)
        val albumsFlow = albumsFlow(coroutineScope, pagingConfig)
        return combine(
            localAudioRepository.getTracksFlow(),
            localAudioRepository.getAlbumsFlow(),
        ) { tracks, albums ->
            if (tracks.isEmpty() && albums.isEmpty()) {
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
        tracksPager(pagingConfig).flow.cachedIn(coroutineScope),
        mediaSessionManager.loadedMediaItem,
        mediaSessionManager.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { track ->
            MediaItemUi(
                mediaItem = track,
                isPlaying = isPlaying && track.id == loadedMediaItem?.id
            )
        }
    }.cachedIn(coroutineScope)

    private fun tracksPager(pagingConfig: PagingConfig) = Pager(
        config = pagingConfig,
        pagingSourceFactory = localAudioRepository::getTrackPagingSource,
    )

    private fun albumsFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ) = combine(
        albumsPager(pagingConfig).flow.cachedIn(coroutineScope),
        mediaSessionManager.loadedMediaItem,
        mediaSessionManager.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { album ->
            MediaItemUi(
                mediaItem = album,
                isPlaying = isPlaying && album.id == loadedMediaItem?.id
            )
        }
    }.cachedIn(coroutineScope)

    private fun albumsPager(pagingConfig: PagingConfig) = Pager(
        config = pagingConfig,
        pagingSourceFactory = localAudioRepository::getAlbumPagingSource,
    )
}
