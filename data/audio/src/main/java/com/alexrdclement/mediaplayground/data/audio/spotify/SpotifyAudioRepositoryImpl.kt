package com.alexrdclement.mediaplayground.data.audio.spotify

import androidx.paging.PagingSource
import com.adamratzman.spotify.SpotifyClientApi
import com.adamratzman.spotify.auth.SpotifyDefaultCredentialStore
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toAlbum
import com.alexrdclement.mediaplayground.data.audio.spotify.mapper.toTrack
import com.alexrdclement.mediaplayground.data.audio.spotify.pagination.SpotifySavedAlbumsPagingSource
import com.alexrdclement.mediaplayground.data.audio.spotify.pagination.SpotifySavedTracksPagingSource
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
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

    override fun getSavedTracksPagingSource(): PagingSource<Int, Track> {
        return SpotifySavedTracksPagingSource(credentialStore)
    }

    override suspend fun getSavedAlbums(): List<Album> {
        // TODO: error handling
        val api = spotifyApi ?: return listOf()
        return api.library.getSavedAlbums().mapNotNull { it?.album?.toAlbum() }
    }

    override fun getSavedAlbumsPagingSource(): PagingSource<Int, Album> {
        return SpotifySavedAlbumsPagingSource(credentialStore)
    }
}
