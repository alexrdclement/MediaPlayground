package com.alexrdclement.mediaplayground.data.audio.spotify

import com.alexrdclement.mediaplayground.data.audio.model.Album
import com.alexrdclement.mediaplayground.data.audio.model.Track

interface SpotifyAudioRepository {
    suspend fun getSavedTracks(): List<Track>
    suspend fun getSavedAlbums(): List<Album>
}
