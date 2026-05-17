package com.alexrdclement.mediaplayground.data.artist

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun getArtistFlow(id: ArtistId): Flow<Artist?>
    suspend fun getArtistByName(name: String): Artist?
    suspend fun getArtistAlbumCount(artistId: ArtistId): Int
    suspend fun put(artist: Artist)
    suspend fun updateArtistName(id: ArtistId, name: String?)
    suspend fun updateArtistNotes(id: ArtistId, notes: String?)
    suspend fun delete(id: ArtistId)
}
