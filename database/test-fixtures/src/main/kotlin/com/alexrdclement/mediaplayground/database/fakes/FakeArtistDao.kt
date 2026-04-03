package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.model.Artist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeArtistDao : ArtistDao {

    val artists = MutableStateFlow(emptySet<Artist>())

    override suspend fun getArtist(id: String): Artist? {
        return artists.value.find { it.id == id }
    }

    override fun getArtistFlow(id: String): Flow<Artist?> {
        return artists.map { it.find { artist -> artist.id == id } }
    }

    override suspend fun getArtistByName(name: String): Artist? {
        return artists.value.find { it.name == name }
    }

    override suspend fun insert(vararg artist: Artist) {
        for (newArtist in artist) {
            if (artists.value.any { it.id == newArtist.id }) continue
            artists.value += newArtist
        }
    }

    override suspend fun update(artist: Artist) {
        val existing = artists.value.find { it.id == artist.id } ?: return
        artists.value = artists.value - existing + artist
    }

    override suspend fun delete(id: String) {
        val existingArtist = artists.value.find { it.id == id } ?: return
        artists.value -= existingArtist
    }
}
