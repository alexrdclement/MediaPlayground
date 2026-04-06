package com.alexrdclement.mediaplayground.media.store

import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId

interface ArtistMediaStore {
    suspend fun getArtistByName(name: String): Artist?

    context(scope: MediaStoreTransactionScope)
    suspend fun getArtistAlbumCount(artistId: ArtistId): Int

    context(scope: MediaStoreTransactionScope)
    suspend fun put(artist: Artist)
}
