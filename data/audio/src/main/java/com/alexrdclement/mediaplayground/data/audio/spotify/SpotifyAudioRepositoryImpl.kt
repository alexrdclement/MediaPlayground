package com.alexrdclement.mediaplayground.data.audio.spotify

import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.alexrdclement.mediaplayground.data.audio.model.Album
import com.alexrdclement.mediaplayground.data.audio.model.SimpleAlbum
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toTrack
import com.alexrdclement.mediaplayground.data.audio.model.Track
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toAlbum
import javax.inject.Inject

class SpotifyAudioRepositoryImpl @Inject constructor(
    private val credentialStore: SpotifyDefaultCredentialStore,
) : SpotifyAudioRepository {

    // TODO: write suspending version of getter
    private val spotifyApi: SpotifyClientApi?
        get() = credentialStore.getSpotifyClientPkceApi()

    override suspend fun getSavedTracks(): List<Track> {
        // TODO: error handling
        val api = spotifyApi ?: return listOf()
        return api.library.getSavedTracks().mapNotNull { it?.track?.toTrack() }
    }

    override suspend fun getSavedAlbums(): List<Album> {
        // TODO: error handling
        val api = spotifyApi ?: return listOf()
        return api.library.getSavedAlbums().mapNotNull { it?.album?.toAlbum() }
    }
}
