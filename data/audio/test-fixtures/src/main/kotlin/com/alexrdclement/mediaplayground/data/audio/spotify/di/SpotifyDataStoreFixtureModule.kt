package com.alexrdclement.mediaplayground.data.audio.spotify.di

import com.alexrdclement.mediaplayground.data.audio.spotify.fakes.FakeSpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.data.audio.spotify.di.SpotifyDataStoreModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SpotifyDataStoreModule::class],
)
abstract class SpotifyDataStoreFixtureModule {
    @Binds
    abstract fun bindSpotifyRemoteDataStore(
        spotifyRemoteDataStore: FakeSpotifyRemoteDataStore
    ): SpotifyRemoteDataStore
}
