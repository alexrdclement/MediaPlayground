package com.alexrdclement.mediaplayground.database.fakes

/**
 * The fake DAOs that back the composite fakes ([FakeCompleteTrackDao], [FakeCompleteAlbumDao]).
 */
data class FakeDaos(
    val albumDao: FakeAlbumDao = FakeAlbumDao(),
    val artistDao: FakeArtistDao = FakeArtistDao(),
    val albumArtistDao: FakeAlbumArtistDao = FakeAlbumArtistDao(),
    val albumImageDao: FakeAlbumImageDao = FakeAlbumImageDao(),
    val imageDao: FakeImageDao = FakeImageDao(),
    val trackDao: FakeTrackDao = FakeTrackDao(),
)
