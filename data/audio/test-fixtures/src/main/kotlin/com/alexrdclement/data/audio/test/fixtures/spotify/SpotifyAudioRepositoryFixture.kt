package com.alexrdclement.data.audio.test.fixtures.spotify

import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepositoryImpl

class SpotifyAudioRepositoryFixture {

    val spotifyRemoteDataStore = FakeSpotifyRemoteDataStore()

    val spotifyAudioRepository = SpotifyAudioRepositoryImpl(
        spotifyRemoteDataStore = spotifyRemoteDataStore,
    )
}
