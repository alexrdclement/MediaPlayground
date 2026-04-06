package com.alexrdclement.mediaplayground.data.artist

import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistDataStore
import com.alexrdclement.mediaplayground.media.model.Artist
import com.alexrdclement.mediaplayground.media.model.ArtistId
import com.alexrdclement.mediaplayground.media.store.ArtistMediaStore
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionScope
import dev.zacsweers.metro.Inject

class ArtistMediaStoreImpl @Inject constructor(
    private val localArtistDataStore: LocalArtistDataStore,
) : ArtistMediaStore {

    override suspend fun getArtistByName(name: String): Artist? =
        localArtistDataStore.getArtistByName(name)

    context(scope: MediaStoreTransactionScope)
    override suspend fun put(artist: Artist) =
        localArtistDataStore.put(artist)

    context(scope: MediaStoreTransactionScope)
    override suspend fun getArtistAlbumCount(artistId: ArtistId) =
        localArtistDataStore.getArtistAlbumCount(artistId = artistId)
}
