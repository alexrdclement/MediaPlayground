package com.alexrdclement.mediaplayground.feature.audio.library.content.spotify

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
import com.alexrdclement.mediaplayground.ui.shared.model.MediaItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpotifyContentStateProvider @Inject constructor(
    private val spotifyAuth: SpotifyAuth,
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val mediaSessionManager: MediaSessionManager,
) {

    fun flow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig
    ): Flow<SpotifyContentState> {
        val savedTracks = savedTracksFlow(coroutineScope, pagingConfig)
        val savedAlbums = savedAlbumsFlow(coroutineScope, pagingConfig)
        return spotifyAuth.isLoggedIn
            .map { isLoggedIn ->
                if (!isLoggedIn) {
                    return@map SpotifyContentState.NotLoggedIn
                }

                return@map SpotifyContentState.LoggedIn(
                    savedTracks = savedTracks,
                    savedAlbums = savedAlbums,
                )
            }
    }

    private fun savedTracksFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ): Flow<PagingData<MediaItemUi>> = combine(
        savedTracksPager(pagingConfig).flow.cachedIn(coroutineScope),
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

    private fun savedAlbumsFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ) = combine(
        savedAlbumsPager(pagingConfig).flow.cachedIn(coroutineScope),
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

    private fun savedTracksPager(pagingConfig: PagingConfig) = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedTracksPagingSource,
    )

    private fun savedAlbumsPager(pagingConfig: PagingConfig) = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedAlbumsPagingSource,
    )
}
