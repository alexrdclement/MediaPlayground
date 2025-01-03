package com.alexrdclement.mediaplayground.data.audio.local.fixtures

import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioDataStore
import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import com.alexrdclement.mediaplayground.data.audio.local.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbumArtistDao
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeArtistDao
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteTrackDao
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeImageDao
import com.alexrdclement.mediaplayground.database.fakes.FakeSimpleAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeTrackDao
import com.alexrdclement.mediaplayground.model.audio.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalAudioDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
    val transactionRunner: FakeDatabaseTransactionRunner = FakeDatabaseTransactionRunner(),
    val artistDao: FakeArtistDao = FakeArtistDao(),
    val albumDao: FakeAlbumDao = FakeAlbumDao(),
    val imageDao: FakeImageDao = FakeImageDao(),
    val trackDao: FakeTrackDao = FakeTrackDao(),
    val albumArtistDao: FakeAlbumArtistDao = FakeAlbumArtistDao(),
    val completeTrackDao: FakeCompleteTrackDao = FakeCompleteTrackDao(
        coroutineScope = coroutineScope,
        albumDao = albumDao,
        artistDao = artistDao,
        albumArtistDao = albumArtistDao,
        imageDao = imageDao,
        trackDao = trackDao,
    ),
    val completeAlbumDao: FakeCompleteAlbumDao = FakeCompleteAlbumDao(
        coroutineScope = coroutineScope,
        albumDao = albumDao,
        artistDao = artistDao,
        albumArtistDao = albumArtistDao,
        imageDao = imageDao,
        trackDao = trackDao,
    ),
    val simpleAlbumDao: FakeSimpleAlbumDao = FakeSimpleAlbumDao(
        albumDao = albumDao,
        artistDao = artistDao,
        imageDao = imageDao,
        albumArtistDao = albumArtistDao,
    ),
    val pathProvider: PathProvider = FakePathProvider(),
) {
    val localAudioDataStore = LocalAudioDataStore(
        transactionRunner = transactionRunner,
        artistDao = artistDao,
        albumDao = albumDao,
        imageDao = imageDao,
        trackDao = trackDao,
        albumArtistDao = albumArtistDao,
        completeTrackDao = completeTrackDao,
        completeAlbumDao = completeAlbumDao,
        simpleAlbumDao = simpleAlbumDao,
        pathProvider = pathProvider,
    )

    suspend fun putTrack(track: Track) {
        localAudioDataStore.putTrack(track)
    }

    suspend fun putTracks(tracks: List<Track>) {
        tracks.forEach { putTrack(it) }
    }

    suspend fun deleteTrack(track: Track) {
        localAudioDataStore.deleteTrack(track)
    }

    suspend fun deleteTracks(tracks: List<Track>) {
        tracks.forEach { deleteTrack(it) }
    }
}
