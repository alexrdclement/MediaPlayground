package com.alexrdclement.mediaplayground.data.audio.spotify.fixtures

import com.alexrdclement.mediaplayground.data.audio.spotify.fakes.FakeSpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepositoryImpl

class SpotifyAudioRepositoryFixture(
    val spotifyRemoteDataStore: FakeSpotifyRemoteDataStore = FakeSpotifyRemoteDataStore()
) {
    val spotifyAudioRepository = SpotifyAudioRepositoryImpl(
        spotifyRemoteDataStore = spotifyRemoteDataStore,
    )
}
