package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.data.audio.model.Album
import com.alexrdclement.mediaplayground.data.audio.model.Track
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
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
}
