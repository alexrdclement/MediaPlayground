package com.alexrdclement.mediaplayground.data.audio.spotify

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result

const val SpotifyRemoteDataStoreDefaultFetchLimit: Int = 50

interface SpotifyRemoteDataStore {
    sealed class Failure {
        data object Timeout : Failure()
        data class Unexpected(val t: Throwable? = null) : Failure()
    }

    data class ListFetchSuccess<T>(
        val items: List<T>,
        val numTotalItems: Int,
    )

    suspend fun getSavedTracks(
        limit: Int = SpotifyRemoteDataStoreDefaultFetchLimit,
        offset: Int = 0,
    ): Result<ListFetchSuccess<Track>, Failure>

    suspend fun getSavedAlbums(
        limit: Int = SpotifyRemoteDataStoreDefaultFetchLimit,
        offset: Int = 0,
    ): Result<ListFetchSuccess<Album>, Failure>

    suspend fun getAlbum(id: String): Result<Album?, Failure>

    suspend fun getTrack(id: String): Result<Track?, Failure>
}
