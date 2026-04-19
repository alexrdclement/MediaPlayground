package com.alexrdclement.mediaplayground.data.track.local

import com.alexrdclement.mediaplayground.data.album.local.LocalAudioAlbumDataStore
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistDataStore
import com.alexrdclement.mediaplayground.data.image.local.LocalImageDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionScope
import com.alexrdclement.media.store.FakeMediaStoreTransactionRunner
import com.alexrdclement.mediaplayground.media.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalTrackDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
) {
    val transactionScope: FakeDatabaseTransactionScope =
        FakeDatabaseTransactionScope(coroutineScope)
    val databaseTransactionRunner: FakeDatabaseTransactionRunner =
        FakeDatabaseTransactionRunner(transactionScope)
    val mediaStoreTransactionRunner: FakeMediaStoreTransactionRunner =
        FakeMediaStoreTransactionRunner()

    val localImageDataStore = LocalImageDataStore(
        imageAssetDao = transactionScope.imageAssetDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )

    val localArtistDataStore = LocalArtistDataStore(
        artistDao = transactionScope.artistDao,
        albumArtistDao = transactionScope.albumArtistDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )

    val localAudioAlbumDataStore = LocalAudioAlbumDataStore(
        simpleAlbumDao = transactionScope.simpleAlbumDao,
        completeAlbumDao = transactionScope.completeAlbumDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )

    val localTrackDataStore = LocalTrackDataStore(
        completeTrackDao = transactionScope.completeTrackDao,
        databaseTransactionRunner = databaseTransactionRunner,
    )

    val albumDao get() = transactionScope.albumDao
    val trackDao get() = transactionScope.trackDao
    val clipDao get() = transactionScope.clipDao
    val trackClipDao get() = transactionScope.trackClipDao
    val imageDao get() = transactionScope.imageAssetDao
    val artistDao get() = transactionScope.artistDao

    suspend fun putTrack(track: Track) {
        mediaStoreTransactionRunner.run {
            for (artist in track.artists) {
                localArtistDataStore.put(artist)
            }
            localImageDataStore.put(track.simpleAlbum.images.toSet())
            localAudioAlbumDataStore.put(track.simpleAlbum)
            localTrackDataStore.put(track)
        }
    }

    suspend fun putTracks(tracks: List<Track>) {
        tracks.forEach { putTrack(it) }
    }

    suspend fun deleteTrack(track: Track) {
        mediaStoreTransactionRunner.run {
            val trackCount = localAudioAlbumDataStore.getAlbumTrackCount(track.simpleAlbum.id)
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

            localAudioAlbumDataStore.delete(track.simpleAlbum.id)
            localTrackDataStore.delete(track.id)
        }
    }

    suspend fun deleteTracks(tracks: List<Track>) {
        tracks.forEach { deleteTrack(it) }
    }
}
