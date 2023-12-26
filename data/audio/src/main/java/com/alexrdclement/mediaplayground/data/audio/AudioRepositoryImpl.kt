package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId
import com.alexrdclement.mediaplayground.model.result.Result
import com.alexrdclement.mediaplayground.model.result.mapFailure
import javax.inject.Inject

class AudioRepositoryImpl @Inject constructor(
    private val spotifyAudioRepository: SpotifyAudioRepository,
) : AudioRepository {
    override suspend fun getSavedTracks(): List<Track> {
        return spotifyAudioRepository.getSavedTracks()
    }

    override suspend fun getSavedAlbums(): List<Album> {
        return spotifyAudioRepository.getSavedAlbums()
    }

    override suspend fun getAlbum(id: AlbumId): Result<Album?, AudioRepository.Failure> {
        return spotifyAudioRepository.getAlbum(id.value)
            .mapFailure(::mapSpotifyAudioRepositoryFailure)
    }

    override suspend fun getTrack(id: TrackId): Result<Track?, AudioRepository.Failure> {
        return spotifyAudioRepository.getTrack(id.value)
            .mapFailure(::mapSpotifyAudioRepositoryFailure)
    }

    private fun mapSpotifyAudioRepositoryFailure(
        failure: SpotifyAudioRepository.Failure
    ): AudioRepository.Failure {
        return when (failure) {
            SpotifyAudioRepository.Failure.Timeout -> AudioRepository.Failure.Timeout
            is SpotifyAudioRepository.Failure.Unexpected -> AudioRepository.Failure.Unexpected(failure.t)
        }
    }
}
