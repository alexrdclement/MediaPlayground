package com.alexrdclement.mediaplayground.data.track.local

import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumDataStore
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistDataStore
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.image.local.LocalImageDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.mediaplayground.database.fakes.FakeMediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalTrackDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
    val pathProvider: PathProvider = FakePathProvider(),
) {
    val transactionScope: FakeDatabaseTransactionScope =
        FakeDatabaseTransactionScope(coroutineScope)
    val databaseTransactionRunner: FakeDatabaseTransactionRunner =
        FakeDatabaseTransactionRunner(transactionScope)
    val mediaStoreTransactionRunner: FakeMediaStoreTransactionRunner =
        FakeMediaStoreTransactionRunner()

    val localImageDataStore = LocalImageDataStore(
        imageFileDao = transactionScope.imageFileDao,
        databaseTransactionRunner = databaseTransactionRunner,
        pathProvider = pathProvider,
    )

    val localArtistDataStore = LocalArtistDataStore(
        artistDao = transactionScope.artistDao,
        albumArtistDao = transactionScope.albumArtistDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )

    val localAlbumDataStore = LocalAlbumDataStore(
        simpleAlbumDao = transactionScope.simpleAlbumDao,
        completeAlbumDao = transactionScope.completeAlbumDao,
        databaseTransactionRunner = databaseTransactionRunner,
        pathProvider = pathProvider,
    )

    val localTrackDataStore = LocalTrackDataStore(
        completeTrackDao = transactionScope.completeTrackDao,
        databaseTransactionRunner = databaseTransactionRunner,
        pathProvider = pathProvider,
    )

    val albumDao get() = transactionScope.albumDao
    val trackDao get() = transactionScope.trackDao
    val imageDao get() = transactionScope.imageFileDao
    val artistDao get() = transactionScope.artistDao

    suspend fun putTrack(track: Track) {
        mediaStoreTransactionRunner.run {
            for (artist in track.artists) {
                localArtistDataStore.put(artist)
            }
            localImageDataStore.put(track.simpleAlbum.images.toSet())
            localAlbumDataStore.put(track.simpleAlbum)
            localTrackDataStore.put(track)
        }
    }

    suspend fun putTracks(tracks: List<Track>) {
        tracks.forEach { putTrack(it) }
    }

    suspend fun deleteTrack(track: Track) {
        mediaStoreTransactionRunner.run {
            val trackCount = localAlbumDataStore.getAlbumTrackCount(track.simpleAlbum.id)
            if (trackCount > 1) {
                localTrackDataStore.delete(track.id)
                return@run
            }

            for (artist in track.artists) {
                val albumCount = localArtistDataStore.getArtistAlbumCount(artist.id)
                if (albumCount <= 1) {
                    localArtistDataStore.delete(artist.id)
                }
            }

            for (image in track.images) {
                localImageDataStore.delete(image.id)
            }

            localAlbumDataStore.delete(track.simpleAlbum.id)
            localTrackDataStore.delete(track.id)
        }
    }

    suspend fun deleteTracks(tracks: List<Track>) {
        tracks.forEach { deleteTrack(it) }
    }
}
