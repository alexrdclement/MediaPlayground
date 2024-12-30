package com.alexrdclement.mediaplayground.data.audio.local.fixtures

import com.alexrdclement.media.mediaimport.fixtures.MediaImporterFixture
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepositoryImpl
import com.alexrdclement.mediaplayground.data.audio.local.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.model.audio.Album
import com.alexrdclement.mediaplayground.model.audio.Track

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

    suspend fun stubTracks(tracks: List<Track>) {
        localAudioDataStoreFixture.stubTracks(tracks)
    }

    suspend fun stubAlbums(albums: List<Album>) {
        localAudioDataStoreFixture.stubAlbums(albums)
    }
}
