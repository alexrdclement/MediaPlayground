package com.alexrdclement.mediaplayground.data.audio.spotify

import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository.*
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toAlbum
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toTrack
import com.alexrdclement.mediaplayground.data.audio.spotify.pagination.SpotifySavedAlbumsPagingSource
import com.alexrdclement.mediaplayground.data.audio.spotify.pagination.SpotifySavedTracksPagingSource
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.map
import com.alexrdclement.mediaplayground.model.result.successOrDefault
import javax.inject.Inject

class SpotifyAudioRepositoryImpl @Inject constructor(
    private val spotifyRemoteDataStore: SpotifyRemoteDataStore,
) : SpotifyAudioRepository {

    override suspend fun getSavedTracks(
        limit: Int,
        offset: Int
    ): Result<ListFetchSuccess<Track>, Failure> {
        return spotifyRemoteDataStore.getSavedTracks(
            limit = limit,
            offset = offset,
        )
            .mapRemoteFailure()
            .map {
                ListFetchSuccess(
                    items = it.items,
                    numTotalItems = it.numTotalItems,
                )
            }
    }

    override fun getSavedTracksPagingSource(): PagingSource<Int, Track> {
        return SpotifySavedTracksPagingSource(spotifyRemoteDataStore)
    }

    override suspend fun getSavedAlbums(
        limit: Int,
        offset: Int
    ): Result<ListFetchSuccess<Album>, Failure> {
        return spotifyRemoteDataStore.getSavedAlbums(
            limit = limit,
            offset = offset,
        )
            .mapRemoteFailure()
            .map {
                ListFetchSuccess(
                    items = it.items,
                    numTotalItems = it.numTotalItems,
                )
            }
    }

    override fun getSavedAlbumsPagingSource(): PagingSource<Int, Album> {
        return SpotifySavedAlbumsPagingSource(spotifyRemoteDataStore)
    }

    override suspend fun getAlbum(id: AlbumId): Result<Album?, Failure> {
        return spotifyRemoteDataStore.getAlbum(id = id).mapRemoteFailure()
    }

    override suspend fun getTrack(id: TrackId): Result<Track?, Failure> {
        return spotifyRemoteDataStore.getTrack(id = id).mapRemoteFailure()
    }

    private fun <T> Result<T, SpotifyRemoteDataStore.Failure>.mapRemoteFailure(): Result<T, Failure> {
        return when (this) {
            is Result.Failure -> {
                val failure = when (val failure = this.failure) {
                    SpotifyRemoteDataStore.Failure.Timeout -> Failure.Timeout
                    is SpotifyRemoteDataStore.Failure.Unexpected -> Failure.Unexpected(failure.t)
                }
                Result.Failure(failure)
            }
            is Result.Success -> Result.Success(this.value)
        }
    }
}
