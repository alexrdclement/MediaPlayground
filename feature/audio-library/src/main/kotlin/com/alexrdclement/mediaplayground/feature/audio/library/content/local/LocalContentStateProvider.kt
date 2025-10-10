package com.alexrdclement.mediaplayground.feature.audio.library.content.local

import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class LocalContentStateProvider @Inject constructor(
    private val localAudioRepository: LocalAudioRepository,
    private val mediaSessionState: MediaSessionState,
) {

    fun flow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ): Flow<LocalContentState> {
        val tracksFlow = tracksFlow(coroutineScope, pagingConfig)
        val albumsFlow = albumsFlow(coroutineScope, pagingConfig)
        return combine(
            localAudioRepository.getTrackCountFlow(),
            localAudioRepository.getAlbumCountFlow(),
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
        localAudioRepository.getTrackPagingData(pagingConfig).cachedIn(coroutineScope),
        mediaSessionState.loadedMediaItem,
        mediaSessionState.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { track ->
            MediaItemUi(
                mediaItem = track,
                isPlaying = isPlaying && track.id == loadedMediaItem?.id
            )
        }
    }.cachedIn(coroutineScope)

    private fun albumsFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ) = combine(
        localAudioRepository.getAlbumPagingData(pagingConfig).cachedIn(coroutineScope),
        mediaSessionState.loadedMediaItem,
        mediaSessionState.isPlaying,
    ) { pagingData, loadedMediaItem, isPlaying ->
        pagingData.map { album ->
            MediaItemUi(
                mediaItem = album,
                isPlaying = isPlaying && album.id == loadedMediaItem?.id
            )
        }
    }.cachedIn(coroutineScope)
}
