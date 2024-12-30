package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef

class FakeAlbumArtistDao : AlbumArtistDao {

    val albumArtists = mutableSetOf<AlbumArtistCrossRef>()

    override suspend fun insert(vararg albumArist: AlbumArtistCrossRef) {
        albumArtists.addAll(albumArist)
    }

    override suspend fun getAlbumArtists(albumId: String): List<AlbumArtistCrossRef> {
        return albumArtists.filter { it.albumId == albumId }
    }

    override suspend fun getArtistAlbums(artistId: String): List<AlbumArtistCrossRef> {
        return albumArtists.filter { it.artistId == artistId }
    }

    override suspend fun delete(albumArtist: AlbumArtistCrossRef) {
        albumArtists.remove(albumArtist)
    }
}
