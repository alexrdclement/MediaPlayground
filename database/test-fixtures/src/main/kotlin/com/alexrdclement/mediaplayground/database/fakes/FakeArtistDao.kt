package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.model.Artist
import kotlinx.coroutines.flow.MutableStateFlow

class FakeArtistDao : ArtistDao {

    val artists = MutableStateFlow(emptySet<Artist>())

    override suspend fun getArtist(id: String): Artist? {
        return artists.value.find { it.id == id }
    }

    override suspend fun getArtistByName(name: String): Artist? {
        return artists.value.find { it.name == name }
    }

    override suspend fun insert(vararg artist: Artist) {
        for (newArtist in artist) {
            val existingArtist = artists.value.find { it.id == newArtist.id }
            if (existingArtist != null) {
                artists.value -= existingArtist
            }
            artists.value += newArtist
        }
    }

    override suspend fun delete(id: String) {
        val existingArtist = artists.value.find { it.id == id } ?: return
        artists.value -= existingArtist
    }
}
