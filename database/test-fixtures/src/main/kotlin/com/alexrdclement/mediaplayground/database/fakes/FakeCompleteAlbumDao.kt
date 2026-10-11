package com.alexrdclement.mediaplayground.database.fakes

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class FakeCompleteAlbumDao(
    val coroutineScope: CoroutineScope,
    val daos: FakeDaos,
) : CompleteAlbumDao {

    val completeAlbums = combine(
        daos.albumDao.albums,
        daos.artistDao.artists,
        daos.trackDao.tracks,
    ) { albums, artists, tracks ->
        val albumArtists = daos.albumArtistDao.albumArtists
        albums.map { album ->
            val albumImageIds = daos.albumImageDao.albumImages
                .filter { it.albumId == album.id }
                .map { it.imageId }
            CompleteAlbum(
                simpleAlbum = SimpleAlbum(
                    album = album,
                    artists = artists.filter { artist ->
                        albumArtists.contains(AlbumArtistCrossRef(albumId = album.id, artistId = artist.id))
                    },
                    images = daos.imageDao.images.value.filter { it.id in albumImageIds },
                ),
                tracks = tracks.filter { it.albumId == album.id },
            )
        }
    }

    @SuppressLint("VisibleForTests")
    override fun getAlbumsPagingSource(): PagingSource<Int, CompleteAlbum> {
        return completeAlbums.asPagingSourceFactory(coroutineScope).invoke()
    }

    override suspend fun getAlbum(id: String): CompleteAlbum? {
        return completeAlbums.firstOrNull()?.find { it.simpleAlbum.album.id == id }
    }

    override fun getAlbumFlow(id: String): Flow<CompleteAlbum?> {
        return completeAlbums.map { albums -> albums.find { it.simpleAlbum.album.id == id } }
    }
}
