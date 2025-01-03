package com.alexrdclement.mediaplayground.database.fakes

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.testing.asPagingSourceFactory
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull

class FakeCompleteTrackDao(
    val coroutineScope: CoroutineScope,
    val albumDao: FakeAlbumDao,
    val artistDao: FakeArtistDao,
    val albumArtistDao: FakeAlbumArtistDao,
    val imageDao: FakeImageDao,
    val trackDao: FakeTrackDao,
) : CompleteTrackDao {

    val completeTracks = combine(
        albumDao.albums,
        artistDao.artists,
        trackDao.tracks,
    ) { albums, artists, tracks ->
        val albumArtists = albumArtistDao.albumArtists
        tracks.mapNotNull {
            val album = albums.find { album -> album.id == it.albumId } ?: return@mapNotNull null
            CompleteTrack(
                track = it,
                album = album,
                artists = artists.filter { artist ->
                    albumArtists.contains(AlbumArtistCrossRef(it.albumId, artist.id))
                },
                images = imageDao.images.filter { image -> image.albumId == it.albumId },
            )
        }
    }

    @SuppressLint("VisibleForTests")
    override fun getTracksPagingSource(): PagingSource<Int, CompleteTrack> {
        return completeTracks.asPagingSourceFactory(coroutineScope).invoke()
    }

    override suspend fun getTrack(id: String): CompleteTrack? {
        return completeTracks.firstOrNull()?.find { it.track.id == id }
    }
}
