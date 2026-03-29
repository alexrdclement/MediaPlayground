package com.alexrdclement.mediaplayground.data.track.fixtures

import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.data.track.local.LocalTrackRepositoryImpl
import com.alexrdclement.mediaplayground.data.disk.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.media.model.audio.Track

class LocalTrackRepositoryFixture(
    val pathProvider: FakePathProvider = FakePathProvider(),
    val mediaImporterFixture: MediaImporterFixture = MediaImporterFixture(),
    val trackDataStoreFixture: LocalTrackDataStoreFixture = LocalTrackDataStoreFixture(
        pathProvider = pathProvider,
    ),
) {
    val localTrackRepository = LocalTrackRepositoryImpl(
        pathProvider = pathProvider,
        mediaImporter = mediaImporterFixture.mediaImporter,
        localTrackDataStore = trackDataStoreFixture.localTrackDataStore,
        localAlbumRepository = trackDataStoreFixture.localAlbumRepository,
        localArtistRepository = trackDataStoreFixture.localArtistRepository,
    )

    val localAlbumRepository get() = trackDataStoreFixture.localAlbumRepository

    suspend fun putTrack(track: Track) {
        trackDataStoreFixture.putTrack(track)
    }

    suspend fun putTracks(tracks: List<Track>) {
        trackDataStoreFixture.putTracks(tracks)
    }

    suspend fun deleteTrack(track: Track) {
        trackDataStoreFixture.deleteTrack(track)
    }

    suspend fun deleteTracks(tracks: List<Track>) {
        trackDataStoreFixture.deleteTracks(tracks)
    }
}
