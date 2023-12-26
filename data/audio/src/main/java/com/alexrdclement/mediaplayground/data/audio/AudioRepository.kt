package com.alexrdclement.mediaplayground.data.audio

import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.AlbumId
import com.alexrdclement.mediaplayground.model.audio.Track

interface AudioRepository {
    suspend fun getSavedTracks(): List<Track>
    suspend fun getSavedAlbums(): List<Album>
    suspend fun getAlbum(id: AlbumId): Album?
}
