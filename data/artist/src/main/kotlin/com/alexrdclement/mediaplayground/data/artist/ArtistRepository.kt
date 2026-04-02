package com.alexrdclement.mediaplayground.data.artist

import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {
    fun getArtistFlow(id: String): Flow<SimpleArtist?>
    suspend fun updateArtistName(id: String, name: String?)
    suspend fun updateArtistNotes(id: String, notes: String?)
    suspend fun deleteArtist(id: String)
}
