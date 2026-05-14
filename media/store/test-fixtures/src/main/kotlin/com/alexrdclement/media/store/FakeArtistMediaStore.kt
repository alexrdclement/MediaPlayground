package com.alexrdclement.media.store

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.store.ArtistMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope

class FakeArtistMediaStore : ArtistMediaStore {

    private val artists = mutableMapOf<ArtistId, Artist>()

    override suspend fun getArtistByName(name: String): Artist? =
        artists.values.find { it.name == name }

    context(scope: MediaStoreTransactionScope)
    override suspend fun getArtistAlbumCount(artistId: ArtistId): Int = 0

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(artist: Artist) {
        artists[artist.id] = artist
    }
}
