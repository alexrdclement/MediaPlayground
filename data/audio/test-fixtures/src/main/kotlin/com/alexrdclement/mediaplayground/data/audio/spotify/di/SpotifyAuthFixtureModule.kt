package com.alexrdclement.mediaplayground.data.audio.spotify.di

import com.alexrdclement.mediaplayground.data.audio.spotify.auth.FakeSpotifyAuth
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.data.audio.spotify.di.SpotifyAuthModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SpotifyAuthModule::class]
)
abstract class SpotifyAuthFixtureModule {
    @Binds
    abstract fun bindSpotifyAuth(
        fakeSpotifyAuth: FakeSpotifyAuth
    ): SpotifyAuth
}
