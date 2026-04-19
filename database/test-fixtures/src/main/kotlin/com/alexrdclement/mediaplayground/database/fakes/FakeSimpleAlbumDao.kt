package com.alexrdclement.mediaplayground.database.fakes

import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteImageAsset
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull

class FakeSimpleAlbumDao(
    private val albumDao: FakeAlbumDao,
    private val mediaCollectionDao: FakeMediaCollectionDao,
    private val artistDao: FakeArtistDao,
    private val imageDao: FakeImageAssetDao,
    private val albumArtistDao: FakeAlbumArtistDao,
    private val albumImageDao: FakeAlbumImageDao = FakeAlbumImageDao(),
) : SimpleAlbumDao {

    val albums = combine(
        albumDao.albums,
        mediaCollectionDao.mediaCollections,
        artistDao.artists,
    ) { albums, mediaCollections, artists ->
        val albumArtists = albumArtistDao.albumArtists
        albums.mapNotNull { album ->
            val mediaCollection = mediaCollections.find { it.id == album.id }
                ?: return@mapNotNull null
            val albumImageIds = albumImageDao.albumImages
                .filter { it.albumId == album.id }
                .map { it.imageId }
            SimpleAlbum(
                album = album,
                mediaCollection = mediaCollection,
                artists = artists.filter { artist ->
                    albumArtists.contains(
                        AlbumArtistCrossRef(album.id, artist.id)
                    )
                },
                images = imageDao.images.value
                    .filter { it.id in albumImageIds }
                    .mapNotNull { imageAsset ->
                        val mediaAsset = imageDao.mediaAssetDao.mediaAssets[imageAsset.id]
                            ?: return@mapNotNull null
                        CompleteImageAsset(imageAsset = imageAsset, mediaAsset = mediaAsset)
                    },
            )
        }
    }

    override suspend fun getAlbumByTitleAndArtistId(title: String, artistId: String): SimpleAlbum? {
        return albums.firstOrNull()?.find {
            it.mediaCollection.title == title && it.artists.any { it.id == artistId }
        }
    }
}
