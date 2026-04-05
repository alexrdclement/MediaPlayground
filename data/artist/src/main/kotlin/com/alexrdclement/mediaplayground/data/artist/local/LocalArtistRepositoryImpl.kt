package com.alexrdclement.mediaplayground.data.artist.local

import com.alexrdclement.mediaplayground.media.model.SimpleArtist
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class LocalArtistRepositoryImpl @Inject constructor(
    private val localArtistDataStore: LocalArtistDataStore,
) : LocalArtistRepository {

    override fun getArtistFlow(id: String): Flow<SimpleArtist?> =
        localArtistDataStore.getArtistFlow(id)

    override suspend fun updateArtistName(id: String, name: String?) =
        localArtistDataStore.updateArtistName(id, name)

    override suspend fun updateArtistNotes(id: String, notes: String?) =
        localArtistDataStore.updateArtistNotes(id, notes)

    override suspend fun deleteArtist(id: String) =
        localArtistDataStore.deleteArtist(id)

    override suspend fun getArtistByName(name: String): SimpleArtist? =
        localArtistDataStore.getArtistByName(name)
}
