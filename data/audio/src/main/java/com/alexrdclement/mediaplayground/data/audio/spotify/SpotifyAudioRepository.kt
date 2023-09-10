package com.alexrdclement.mediaplayground.data.audio.spotify

import androidx.paging.PagingSource
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track

interface SpotifyAudioRepository {
    suspend fun getSavedTracks(): List<Track>
    fun getSavedTracksPagingSource(): PagingSource<Int, Track>
    suspend fun getSavedAlbums(): List<Album>
    fun getSavedAlbumsPagingSource(): PagingSource<Int, Album>
    suspend fun getAlbum(id: String): Album?
}
