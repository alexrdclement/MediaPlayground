package com.alexrdclement.mediaplayground.data.audio.spotify.di

import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuth
import com.alexrdclement.mediaplayground.data.audio.spotify.auth.SpotifyAuthImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SpotifyAuthModule {
    @Binds
    abstract fun bindSpotifyAuth(
        spotifyAuthImpl: SpotifyAuthImpl
    ): SpotifyAuth
}
