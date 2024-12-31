package com.alexrdclement.mediaplayground.data.audio.local.fixtures

import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioDataStore
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbumArtistDao
import com.alexrdclement.mediaplayground.database.fakes.FakeAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeArtistDao
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeCompleteTrackDao
import com.alexrdclement.mediaplayground.database.fakes.FakeDatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.fakes.FakeImageDao
import com.alexrdclement.mediaplayground.database.fakes.FakeSimpleAlbumDao
import com.alexrdclement.mediaplayground.database.fakes.FakeTrackDao
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.mapper.toTrack
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
    )

    suspend fun stubTracks(tracks: List<Track>) {
        localAudioDataStore.deleteAllTracks()
        for (track in tracks) {
            localAudioDataStore.putTrack(track = track)
        }
    }

    suspend fun stubAlbums(albums: List<Album>) {
        localAudioDataStore.deleteAllAlbums()
        for (album in albums) {
            for (simpleTrack in album.tracks) {
                val track = simpleTrack.toTrack(album)
                localAudioDataStore.putTrack(track = track)
            }
        }
    }
}
