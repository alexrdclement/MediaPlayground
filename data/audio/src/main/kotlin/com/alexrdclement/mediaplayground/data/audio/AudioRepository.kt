package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result

const val AudioRepositoryDefaultFetchLimit = 50

interface AudioRepository {
    sealed class Failure {
        data object Timeout : Failure()
        data class Unexpected(val t: Throwable? = null) : Failure()
    }

    data class ListFetchSuccess<T>(
        val items: List<T>,
        val numTotalItems: Int,
    )

    suspend fun getSavedTracks(
        limit: Int = AudioRepositoryDefaultFetchLimit,
        offset: Int = 0,
    ): Result<ListFetchSuccess<Track>, Failure>

    suspend fun getSavedAlbums(
        limit: Int = AudioRepositoryDefaultFetchLimit,
        offset: Int = 0,
    ): Result<ListFetchSuccess<Album>, Failure>

    suspend fun getAlbum(id: AlbumId, source: Source? = null): Result<Album?, Failure>

    suspend fun getTrack(id: TrackId, source: Source? = null): Result<Track?, Failure>
}
