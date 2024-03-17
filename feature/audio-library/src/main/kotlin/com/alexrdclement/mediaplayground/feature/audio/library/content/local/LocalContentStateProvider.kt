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
import kotlinx.coroutines.flow.map
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
        return localAudioRepository.getTracks()
            .map { tracks ->
                if (tracks.isEmpty()) {
                    LocalContentState.Empty
                } else {
                    LocalContentState.Content(
                        tracks = tracksFlow
                    )
                }
            }
    }

    private fun tracksFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ) = combine(
        tracksPager(pagingConfig).flow.cachedIn(coroutineScope),
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

    private fun tracksPager(pagingConfig: PagingConfig) = Pager(
        config = pagingConfig,
        pagingSourceFactory = localAudioRepository::getTrackPagingSource,
    )
}
