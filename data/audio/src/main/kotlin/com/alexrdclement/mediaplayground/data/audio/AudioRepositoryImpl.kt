package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.data.audio.AudioRepository.Failure
import com.alexrdclement.mediaplayground.data.audio.AudioRepository.ListFetchSuccess
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.map
import com.alexrdclement.mediaplayground.model.result.mapFailure
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val localAudioRepository: LocalAudioRepository,
    private val spotifyAudioRepository: SpotifyAudioRepository,
) : AudioRepository {

    override suspend fun getSavedTracks(
        limit: Int,
        offset: Int
    ): Result<ListFetchSuccess<Track>, Failure> {
        // TODO: Integrate local
        return spotifyAudioRepository.getSavedTracks(
            limit = limit,
            offset = offset,
        ).mapSpotifyRepositoryFailure()
            .map {
                ListFetchSuccess(
                    items = it.items,
                    numTotalItems = it.numTotalItems
                )
            }
    }

    override suspend fun getSavedAlbums(
        limit: Int,
        offset: Int
    ): Result<ListFetchSuccess<Album>, Failure> {
        // TODO: Integrate local
        return spotifyAudioRepository.getSavedAlbums(
            limit = limit,
            offset = offset,
        ).mapSpotifyRepositoryFailure()
            .map {
                ListFetchSuccess(
                    items = it.items,
                    numTotalItems = it.numTotalItems
                )
            }
    }

    override suspend fun getAlbum(id: AlbumId): Result<Album?, Failure> {
        // TODO: Integrate local
        return spotifyAudioRepository.getAlbum(id.value)
            .mapFailure(::mapSpotifyAudioRepositoryFailure)
    }

    override suspend fun getTrack(id: TrackId): Result<Track?, Failure> {
        // Check local first, fallback to Spotify
        return when (val localTrackResult = localAudioRepository.getTrack(id)) {
            is Result.Success -> Result.Success(localTrackResult.value)
            is Result.Failure -> {
                when (localTrackResult.failure) {
                    LocalAudioRepository.Failure.TrackNotFound -> {
                        spotifyAudioRepository.getTrack(id.value)
                            .mapFailure(::mapSpotifyAudioRepositoryFailure)
                    }
                }
            }
        }
    }

    private fun mapSpotifyAudioRepositoryFailure(
        failure: SpotifyAudioRepository.Failure
    ): Failure {
        return when (failure) {
            SpotifyAudioRepository.Failure.Timeout -> Failure.Timeout
            is SpotifyAudioRepository.Failure.Unexpected -> Failure.Unexpected(failure.t)
        }
    }

    private fun <T> Result<T, SpotifyAudioRepository.Failure>.mapSpotifyRepositoryFailure(): Result<T, Failure> {
        return when (this) {
            is Result.Failure -> {
                val failure = when (val failure = this.failure) {
                    SpotifyAudioRepository.Failure.Timeout -> Failure.Timeout
                    is SpotifyAudioRepository.Failure.Unexpected -> Failure.Unexpected(failure.t)
                }
                Result.Failure(failure)
            }
            is Result.Success -> Result.Success(this.value)
        }
    }
}
