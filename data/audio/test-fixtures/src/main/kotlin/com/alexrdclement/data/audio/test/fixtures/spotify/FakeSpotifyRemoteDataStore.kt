package com.alexrdclement.data.audio.test.fixtures.spotify

import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore.Failure
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore.ListFetchSuccess
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakeSpotifyRemoteDataStore @Inject constructor() : SpotifyRemoteDataStore {

    val mutableSavedTracks = MutableStateFlow(listOf<Track>())
    val mutableSavedAlbums = MutableStateFlow(listOf<Album>())

    override suspend fun getSavedTracks(
        limit: Int,
        offset: Int
    ): Result<ListFetchSuccess<Track>, Failure> {
        val listFetchSuccess = ListFetchSuccess(
            items = mutableSavedTracks.value,
            numTotalItems = mutableSavedTracks.value.size,
        )
        return Result.Success(value = listFetchSuccess)
    }

    override suspend fun getSavedAlbums(
        limit: Int,
        offset: Int
    ): Result<ListFetchSuccess<Album>, Failure> {
        val listFetchSuccess = ListFetchSuccess(
            items = mutableSavedAlbums.value,
            numTotalItems = mutableSavedAlbums.value.size,
        )
        return Result.Success(value = listFetchSuccess)
    }

    override suspend fun getAlbum(id: AlbumId): Result<Album?, Failure> {
        return Result.Success(mutableSavedAlbums.value.firstOrNull { it.id == id })
    }

    override suspend fun getTrack(id: TrackId): Result<Track?, Failure> {
        return Result.Success(mutableSavedTracks.value.firstOrNull { it.id == id })
    }
}
