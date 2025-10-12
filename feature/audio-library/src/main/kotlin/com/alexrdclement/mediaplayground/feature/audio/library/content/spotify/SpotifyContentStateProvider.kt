package com.alexrdclement.mediaplayground.feature.audio.library.content.spotify

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.isPlaying
import com.alexrdclement.mediaplayground.media.session.loadedMediaItem
import com.alexrdclement.mediaplayground.ui.model.MediaItemUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SpotifyContentStateProvider @Inject constructor(
    private val spotifyAuth: SpotifyAuth,
    private val spotifyAudioRepository: SpotifyAudioRepository,
    private val mediaSessionState: MediaSessionState,
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

    private fun savedAlbumsFlow(
        coroutineScope: CoroutineScope,
        pagingConfig: PagingConfig,
    ) = combine(
        savedAlbumsPager(pagingConfig).flow.cachedIn(coroutineScope),
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

    private fun savedTracksPager(pagingConfig: PagingConfig) = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedTracksPagingSource,
    )

    private fun savedAlbumsPager(pagingConfig: PagingConfig) = Pager(
        config = pagingConfig,
        pagingSourceFactory = spotifyAudioRepository::getSavedAlbumsPagingSource,
    )
}
