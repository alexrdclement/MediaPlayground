package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.data.audio.AudioRepository.Failure
import com.alexrdclement.mediaplayground.data.audio.AudioRepository.ListFetchSuccess
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.util.awaitFirst
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Source
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.map
import com.alexrdclement.mediaplayground.model.result.mapFailure
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val localAudioRepository: LocalAudioRepository,
    private val spotifyAudioRepository: SpotifyAudioRepository,
) : AudioRepository {

    override suspend fun getSavedTracks(
        limit: Int,
        offset: Int
    ): Result<ListFetchSuccess<Track>, Failure> {
        // TODO: Local faves
        return spotifyAudioRepository.getSavedTracks(
            limit = limit,
            offset = offset,
        ).mapFailure(::mapSpotifyAudioRepositoryFailure)
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
        // TODO: Local faves
        return spotifyAudioRepository.getSavedAlbums(
            limit = limit,
            offset = offset,
        ).mapFailure(::mapSpotifyAudioRepositoryFailure)
            .map {
                ListFetchSuccess(
                    items = it.items,
                    numTotalItems = it.numTotalItems
                )
            }
    }

    override suspend fun getAlbum(id: AlbumId, source: Source?): Result<Album?, Failure> {
        if (source != null) {
            return getAlbum(id, source)
        }
        return coroutineScope {
            Source.entries.map {
                async {
                    getAlbum(id, it)
                }
            }.awaitFirst { it is Result.Success && it.value != null }
                ?: Result.Failure(Failure.Unexpected())
        }
    }

    override suspend fun getTrack(id: TrackId, source: Source?): Result<Track?, Failure> {
        if (source != null) {
            return getTrack(id, source)
        }
        return coroutineScope {
            Source.entries.map {
                async {
                    getTrack(id, it)
                }
            }.awaitFirst { it is Result.Success && it.value != null }
                ?: Result.Failure(Failure.Unexpected())
        }
    }

    @JvmName("getAlbumWithSource")
    private suspend fun getAlbum(id: AlbumId, source: Source): Result<Album?, Failure> {
        return coroutineScope {
            when (source) {
                Source.Local -> localAudioRepository.getAlbum(id)
                    .mapFailure(::mapLocalAudioRepositoryFailure)

                Source.Spotify -> spotifyAudioRepository.getAlbum(id)
                    .mapFailure(::mapSpotifyAudioRepositoryFailure)
            }
        }
    }

    @JvmName("getTrackWithSource")
    private suspend fun getTrack(id: TrackId, source: Source): Result<Track?, Failure> {
        return coroutineScope {
            when (source) {
                Source.Local -> localAudioRepository.getTrack(id)
                    .mapFailure(::mapLocalAudioRepositoryFailure)

                Source.Spotify -> spotifyAudioRepository.getTrack(id)
                    .mapFailure(::mapSpotifyAudioRepositoryFailure)
            }
        }
    }

    private fun mapLocalAudioRepositoryFailure(
        failure: LocalAudioRepository.Failure
    ): Failure {
        return when (failure) {
            LocalAudioRepository.Failure.TrackNotFound -> Failure.Unexpected()
            LocalAudioRepository.Failure.AlbumNotFound -> Failure.Unexpected()
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
}
