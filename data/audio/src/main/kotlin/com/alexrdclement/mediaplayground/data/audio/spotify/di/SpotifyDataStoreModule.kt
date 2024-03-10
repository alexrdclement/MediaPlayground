package com.alexrdclement.mediaplayground.data.audio.spotify.di

import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStore
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyRemoteDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SpotifyDataStoreModule {
    @Binds
    abstract fun bindSpotifyRemoteDataStore(
        spotifyRemoteDataStore: SpotifyRemoteDataStoreImpl
    ): SpotifyRemoteDataStore
}
