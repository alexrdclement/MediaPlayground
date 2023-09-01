package com.alexrdclement.mediaplayground.data.audio.spotify

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track

interface SpotifyAudioRepository {
    suspend fun getSavedTracks(): List<Track>
    suspend fun getSavedAlbums(): List<Album>
}
