package com.alexrdclement.mediaplayground.data.artist

import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistDataStore
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

class ArtistRepositoryImpl @Inject constructor(
    private val localArtistDataStore: LocalArtistDataStore,
) : ArtistRepository {

    override fun getArtistFlow(id: ArtistId): Flow<Artist?> =
        localArtistDataStore.getArtistFlow(id)

    override suspend fun getArtistAlbumCount(artistId: ArtistId): Int =
        localArtistDataStore.getArtistAlbumCount(artistId)

    override suspend fun updateArtistName(id: ArtistId, name: String?) =
        localArtistDataStore.updateArtistName(id, name)

    override suspend fun updateArtistNotes(id: ArtistId, notes: String?) =
        localArtistDataStore.updateArtistNotes(id, notes)

    override suspend fun delete(id: ArtistId) =
        localArtistDataStore.delete(id)

    override suspend fun getArtistByName(name: String): Artist? =
        localArtistDataStore.getArtistByName(name)

    override suspend fun put(artist: Artist) {
        localArtistDataStore.put(artist)
    }
}
