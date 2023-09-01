package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.data.audio.model.Album
import com.alexrdclement.mediaplayground.data.audio.model.Track

interface AudioRepository {
    suspend fun getSavedTracks(): List<Track>
    suspend fun getSavedAlbums(): List<Album>
}
