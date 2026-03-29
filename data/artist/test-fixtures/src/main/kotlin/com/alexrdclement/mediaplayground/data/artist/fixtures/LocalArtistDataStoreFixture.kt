package com.alexrdclement.mediaplayground.data.artist.fixtures

import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeArtistDao

class LocalArtistDataStoreFixture(
    val artistDao: FakeArtistDao = FakeArtistDao(),
) {
    val localArtistDataStore = LocalArtistDataStore(artistDao = artistDao)
}
