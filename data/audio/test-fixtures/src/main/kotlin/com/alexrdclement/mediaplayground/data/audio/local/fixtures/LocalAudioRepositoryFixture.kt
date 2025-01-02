package com.alexrdclement.mediaplayground.data.audio.local.fixtures

import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepositoryImpl
import com.alexrdclement.mediaplayground.data.audio.local.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.model.audio.SimpleTrack
import com.alexrdclement.mediaplayground.model.audio.Track
import com.alexrdclement.mediaplayground.model.audio.TrackId

class LocalAudioRepositoryFixture(
    val pathProvider: FakePathProvider = FakePathProvider(),
    val mediaImporterFixture: MediaImporterFixture = MediaImporterFixture(),
    val localAudioDataStoreFixture: LocalAudioDataStoreFixture = LocalAudioDataStoreFixture(),
) {
    val localAudioRepository = LocalAudioRepositoryImpl(
        pathProvider = pathProvider,
        mediaImporter = mediaImporterFixture.mediaImporter,
        localAudioDataStore = localAudioDataStoreFixture.localAudioDataStore,
    )

    suspend fun putTrack(track: Track) {
        localAudioDataStoreFixture.putTrack(track)
    }

    suspend fun putTracks(tracks: List<Track>) {
        localAudioDataStoreFixture.putTracks(tracks)
    }

    suspend fun deleteTrack(track: Track) {
        localAudioDataStoreFixture.deleteTrack(track)
    }

    suspend fun deleteTracks(tracks: List<Track>) {
        localAudioDataStoreFixture.deleteTracks(tracks)
    }
}
