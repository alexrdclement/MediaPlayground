package com.alexrdclement.mediaplayground.database.fakes

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteAlbum
import com.alexrdclement.mediaplayground.database.model.SimpleAlbum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull

class FakeCompleteAlbumDao(
    val coroutineScope: CoroutineScope,
    val albumDao: FakeAlbumDao,
    val artistDao: FakeArtistDao,
    val albumArtistDao: FakeAlbumArtistDao,
    val imageDao: FakeImageDao,
    val trackDao: FakeTrackDao,
) : CompleteAlbumDao {

    val completeAlbums = combine(
        albumDao.albums,
        artistDao.artists,
        trackDao.tracks,
    ) { albums, artists, tracks ->
        val albumArtists = albumArtistDao.albumArtists
        albums.map { album ->
            CompleteAlbum(
                simpleAlbum = SimpleAlbum(
                    album = album,
                    artists = artists.filter { artist ->
                        albumArtists.contains(AlbumArtistCrossRef(album.id, artist.id))
                    },
                    images = imageDao.images.filter { it.albumId == album.id },
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

    override suspend fun deleteAll() {
        for (album in completeAlbums.firstOrNull() ?: emptyList()) {
            delete(album.simpleAlbum.album.id)
        }
    }

    override suspend fun delete(id: String) {
        val existingAlbum = completeAlbums.firstOrNull()?.find { it.simpleAlbum.album.id == id } ?: return
        for (track in existingAlbum.tracks) {
            trackDao.delete(track.id)
        }

        for (artist in existingAlbum.simpleAlbum.artists) {
            albumArtistDao.delete(AlbumArtistCrossRef(existingAlbum.simpleAlbum.album.id, artist.id))
            // Delete artist if this is their only album
            albumArtistDao.getArtistAlbums(artist.id).let { artistAlbums ->
                if (artistAlbums.isEmpty()) {
                    artistDao.delete(artist.id)
                }
            }
        }

        for (image in existingAlbum.simpleAlbum.images) {
            imageDao.delete(image.id)
        }
        albumDao.delete(id)
    }
}
