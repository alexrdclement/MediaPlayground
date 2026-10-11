package com.alexrdclement.mediaplayground.data.track.fixtures

import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumDataStore
import com.alexrdclement.mediaplayground.data.album.local.LocalAlbumRepositoryImpl
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistDataStore
import com.alexrdclement.mediaplayground.data.artist.local.LocalArtistRepositoryImpl
import com.alexrdclement.mediaplayground.data.disk.PathProvider
import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDaos
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbumArtistDao
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbumImageDao
import com.alexrdclement.mediaplayground.database.fakes.FakeArtistDao
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteTrackDao
import com.alexrdclement.mediaplayground.database.fakes.FakeDaos
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeImageDao
import com.alexrdclement.mediaplayground.database.fakes.FakeSimpleAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeTrackDao
import com.alexrdclement.mediaplayground.media.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class LocalTrackDataStoreFixture(
    val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate),
    val pathProvider: PathProvider = FakePathProvider(),
) {
    val transactionRunner: FakeDatabaseTransactionRunner = FakeDatabaseTransactionRunner()
    val daos: FakeDaos = FakeDaos()
    val artistDao: FakeArtistDao get() = daos.artistDao
    val albumDao: FakeAlbumDao get() = daos.albumDao
    val imageDao: FakeImageDao get() = daos.imageDao
    val albumImageDao: FakeAlbumImageDao get() = daos.albumImageDao
    val trackDao: FakeTrackDao get() = daos.trackDao
    val albumArtistDao: FakeAlbumArtistDao get() = daos.albumArtistDao
    val completeTrackDao: FakeCompleteTrackDao = FakeCompleteTrackDao(
        coroutineScope = coroutineScope,
        daos = daos,
    )
    val completeAlbumDao: FakeCompleteAlbumDao = FakeCompleteAlbumDao(
        coroutineScope = coroutineScope,
        daos = daos,
    )
    val simpleAlbumDao: FakeSimpleAlbumDao = FakeSimpleAlbumDao(
        albumDao = albumDao,
        artistDao = artistDao,
        imageDao = imageDao,
        albumArtistDao = albumArtistDao,
        albumImageDao = albumImageDao,
    )

    val localArtistDataStore = LocalArtistDataStore(
        artistDao = artistDao,
    )

    val localAlbumDataStore = LocalAlbumDataStore(
        albumDao = albumDao,
        completeAlbumDao = completeAlbumDao,
        simpleAlbumDao = simpleAlbumDao,
        pathProvider = pathProvider,
    )

    val localAlbumRepository = LocalAlbumRepositoryImpl(
        localAlbumDataStore = localAlbumDataStore,
    )

    val localArtistRepository = LocalArtistRepositoryImpl(
        localArtistDataStore = localArtistDataStore,
    )

    val localTrackDataStore = LocalTrackDataStore(
        transactionRunner = transactionRunner,
        daos = LocalTrackDaos(
            artistDao = artistDao,
            albumDao = albumDao,
            imageDao = imageDao,
            albumImageDao = albumImageDao,
            trackDao = trackDao,
            albumArtistDao = albumArtistDao,
            completeTrackDao = completeTrackDao,
        ),
        pathProvider = pathProvider,
    )

    suspend fun putTrack(track: Track) {
        localTrackDataStore.putTrack(track)
    }

    suspend fun putTracks(tracks: List<Track>) {
        tracks.forEach { putTrack(it) }
    }

    suspend fun deleteTrack(track: Track) {
        localTrackDataStore.deleteTrack(track)
    }

    suspend fun deleteTracks(tracks: List<Track>) {
        tracks.forEach { deleteTrack(it) }
    }
}
