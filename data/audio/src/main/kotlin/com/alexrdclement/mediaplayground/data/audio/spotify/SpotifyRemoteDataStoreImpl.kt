package com.alexrdclement.mediaplayground.data.audio.spotify

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore.Failure
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore.ListFetchSuccess
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toAlbum
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toTrack
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.successOrDefault
import javax.inject.Inject

class SpotifyRemoteDataStoreImpl @Inject constructor(
    private val credentialStore: SpotifyDefaultCredentialStore,
) : SpotifyRemoteDataStore {
    // TODO: write suspending version of getter
    private val spotifyApi: SpotifyClientApi?
        get() = credentialStore.getSpotifyClientPkceApi()

    override suspend fun getSavedTracks(
        limit: Int,
        offset: Int,
    ): Result<ListFetchSuccess<Track>, Failure> {
        return execute { api ->
            val response = api.library.getSavedTracks(
                limit = limit,
                offset = offset,
            )
            ListFetchSuccess(
                items = response.mapNotNull { it?.track?.toTrack() },
                numTotalItems = response.total,
            )
        }
    }

    override suspend fun getSavedAlbums(
        limit: Int,
        offset: Int,
    ): Result<ListFetchSuccess<Album>, Failure> {
        return execute { api ->
            val response = api.library.getSavedAlbums(
                limit = limit,
                offset = offset,
            )
            ListFetchSuccess(
                items = response.mapNotNull { it?.album?.toAlbum() },
                numTotalItems = response.total,
            )
        }
    }

    override suspend fun getAlbum(id: String): Result<Album?, Failure> {
        return execute { api ->
            api.albums.getAlbum(id)?.toAlbum()
        }
    }

    override suspend fun getTrack(id: String): Result<Track?, Failure> {
        return execute { api ->
            api.tracks.getTrack(id)?.toTrack()
        }
    }

    private suspend fun <T> execute(
        block: suspend (SpotifyClientApi) -> T,
    ): Result<T, Failure> {
        val spotifyApi = spotifyApi ?: return Result.Failure(Failure.Unexpected())
        return try {
            val result = block(spotifyApi)
            Result.Success(result)
        } catch (e: SpotifyException) {
            val failure = when (e) {
                is SpotifyException.TimeoutException -> {
                    // As of 4.0.2, this is mistakenly thrown on cancellation as well as legitimate
                    // timeout.
                    Failure.Timeout
                }
                is SpotifyException.AuthenticationException,
                is SpotifyException.BadRequestException,
                is SpotifyException.ParseException,
                is SpotifyException.ReAuthenticationNeededException,
                is SpotifyException.UnNullableException -> Failure.Unexpected(e)
            }
            Result.Failure(failure)
        }
    }

}
