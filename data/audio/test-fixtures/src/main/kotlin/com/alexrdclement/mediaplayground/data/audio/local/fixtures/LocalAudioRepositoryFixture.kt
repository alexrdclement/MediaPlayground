package com.alexrdclement.mediaplayground.data.audio.local.fixtures

import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioDataStore
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepositoryImpl
import com.alexrdclement.mediaplayground.data.audio.local.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.model.audio.Track

class LocalAudioRepositoryFixture(
    val pathProvider: FakePathProvider = FakePathProvider(),
    val mediaImporterFixture: MediaImporterFixture = MediaImporterFixture(),
    val localAudioDataStore: LocalAudioDataStore = LocalAudioDataStore(),
) {
    val localAudioRepository = LocalAudioRepositoryImpl(
        pathProvider = pathProvider,
        mediaImporter = mediaImporterFixture.mediaImporter,
        localAudioDataStore = localAudioDataStore,
    )

    fun stubTracks(tracks: List<Track>) {
        localAudioDataStore.clearTracks()
        for (track in tracks) {
            localAudioDataStore.putTrack(track = track)
        }
    }
}
