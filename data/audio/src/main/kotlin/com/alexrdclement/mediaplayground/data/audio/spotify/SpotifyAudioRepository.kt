package com.alexrdclement.mediaplayground.data.audio.spotify

import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result

const val SpotifyAudioRepositoryDefaultFetchLimit: Int = 50

interface SpotifyAudioRepository {
    sealed class Failure {
        data object Timeout : Failure()
        data class Unexpected(val t: Throwable? = null) : Failure()
    }

    data class ListFetchSuccess<T>(
        val items: List<T>,
        val numTotalItems: Int,
    )

    suspend fun getSavedTracks(
        limit: Int = SpotifyAudioRepositoryDefaultFetchLimit,
        offset: Int = 0,
    ): Result<ListFetchSuccess<Track>, Failure>

    fun getSavedTracksPagingSource(): PagingSource<Int, Track>

    suspend fun getSavedAlbums(
        limit: Int = SpotifyAudioRepositoryDefaultFetchLimit,
        offset: Int = 0,
    ): Result<ListFetchSuccess<Album>, Failure>

    fun getSavedAlbumsPagingSource(): PagingSource<Int, Album>

    suspend fun getAlbum(id: AlbumId): Result<Album?, Failure>

    suspend fun getTrack(id: TrackId): Result<Track?, Failure>
}
