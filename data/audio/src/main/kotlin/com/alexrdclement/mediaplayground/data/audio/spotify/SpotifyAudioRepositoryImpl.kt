package com.alexrdclement.mediaplayground.data.audio.spotify

import androidx.paging.PagingSource
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.SpotifyException
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toAlbum
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toTrack
import com.alexrdclement.mediaplayground.data.audio.spotify.pagination.SpotifySavedAlbumsPagingSource
import com.alexrdclement.mediaplayground.data.audio.spotify.pagination.SpotifySavedTracksPagingSource
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.successOrDefault
import javax.inject.Inject

class SpotifyAudioRepositoryImpl @Inject constructor(
    private val credentialStore: SpotifyDefaultCredentialStore,
) : SpotifyAudioRepository {

    // TODO: write suspending version of getter
    private val spotifyApi: SpotifyClientApi?
        get() = credentialStore.getSpotifyClientPkceApi()

    override suspend fun getSavedTracks(): List<Track> {
        // TODO: error handling
        val result = execute { api ->
            api.library.getSavedTracks().mapNotNull { it?.track?.toTrack() }
        }
        return result.successOrDefault(listOf())
    }

    override fun getSavedTracksPagingSource(): PagingSource<Int, Track> {
        return SpotifySavedTracksPagingSource(credentialStore)
    }

    override suspend fun getSavedAlbums(): List<Album> {
        // TODO: Error handling
        val result = execute { api ->
            api.library.getSavedAlbums().mapNotNull { it?.album?.toAlbum() }
        }
        return result.successOrDefault(listOf())
    }

    override fun getSavedAlbumsPagingSource(): PagingSource<Int, Album> {
        return SpotifySavedAlbumsPagingSource(credentialStore)
    }

    override suspend fun getAlbum(id: String): Result<Album?, SpotifyAudioRepository.Failure> {
        return execute { api ->
            api.albums.getAlbum(id)?.toAlbum()
        }
    }

    override suspend fun getTrack(id: String): Result<Track?, SpotifyAudioRepository.Failure> {
        return execute { api ->
            api.tracks.getTrack(id)?.toTrack()
        }
    }

    private suspend fun <T> execute(
        block: suspend (SpotifyClientApi) -> T,
    ): Result<T, SpotifyAudioRepository.Failure> {
        val spotifyApi = spotifyApi ?: return Result.Failure(SpotifyAudioRepository.Failure.Unexpected())
        return try {
            val result = block(spotifyApi)
            Result.Success(result)
        } catch (e: SpotifyException) {
            val failure = when (e) {
                is SpotifyException.TimeoutException -> {
                    // As of 4.0.2, this is mistakenly thrown on cancellation as well as legitimate
                    // timeout.
                    SpotifyAudioRepository.Failure.Timeout
                }
                is SpotifyException.AuthenticationException,
                is SpotifyException.BadRequestException,
                is SpotifyException.ParseException,
                is SpotifyException.ReAuthenticationNeededException,
                is SpotifyException.UnNullableException -> SpotifyAudioRepository.Failure.Unexpected(e)
            }
            Result.Failure(failure)
        }
    }
}
