package com.alexrdclement.mediaplayground.data.artist.local

import com.alexrdclement.mediaplayground.data.artist.local.mapper.toSimpleArtist
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.media.model.audio.SimpleArtist
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalArtistDataStore @Inject constructor(
    private val artistDao: ArtistDao,
) {
    suspend fun getArtist(artistId: String): SimpleArtist? {
        return artistDao.getArtist(artistId)?.toSimpleArtist()
    }

    fun getArtistFlow(artistId: String): Flow<SimpleArtist?> {
        return artistDao.getArtistFlow(artistId).map { it?.toSimpleArtist() }
    }

    suspend fun getArtistByName(name: String): SimpleArtist? {
        return artistDao.getArtistByName(name)?.toSimpleArtist()
    }

    suspend fun updateArtistName(artistId: String, name: String?) {
        val artist = artistDao.getArtist(artistId) ?: return
        artistDao.update(artist.copy(name = name))
    }

    suspend fun updateArtistNotes(artistId: String, notes: String?) {
        val artist = artistDao.getArtist(artistId) ?: return
        artistDao.update(artist.copy(notes = notes))
    }

    suspend fun deleteArtist(artistId: String) {
        artistDao.delete(artistId)
    }
}
