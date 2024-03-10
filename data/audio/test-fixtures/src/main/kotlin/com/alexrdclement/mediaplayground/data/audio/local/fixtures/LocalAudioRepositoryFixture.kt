package com.alexrdclement.mediaplayground.data.audio.local.fixtures

import com.alexrdclement.mediaplayground.data.audio.local.fakes.FakePathProvider
import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioDataStore
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepositoryImpl
import com.alexrdclement.mediaplayground.model.audio.Track

class LocalAudioRepositoryFixture {

    val pathProvider = FakePathProvider()
    val mediaImporterFixture = MediaImporterFixture()
    val localAudioDataStore = LocalAudioDataStore()

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
