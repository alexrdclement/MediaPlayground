package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull

class FakeSimpleAlbumDao(
    private val albumDao: FakeAlbumDao,
    private val artistDao: FakeArtistDao,
    private val imageDao: FakeImageDao,
    private val albumArtistDao: FakeAlbumArtistDao,
) : SimpleAlbumDao {

    val albums = combine(
        albumDao.albums,
        artistDao.artists,
    ) { albums, artists ->
        val albumArtists = albumArtistDao.albumArtists
        albums.map { album ->
            SimpleAlbum(
                album = album,
                artists = artists.filter { artist ->
                    albumArtists.contains(
                        AlbumArtistCrossRef(album.id, artist.id)
                    )
                },
                images = imageDao.images.filter { it.albumId == album.id },
            )
        }
    }

    override suspend fun getAlbumByTitleAndArtistId(title: String, artistId: String): SimpleAlbum? {
        return albums.firstOrNull()?.find {
            it.album.title == title && it.artists.any { it.id == artistId }
        }
    }
}
