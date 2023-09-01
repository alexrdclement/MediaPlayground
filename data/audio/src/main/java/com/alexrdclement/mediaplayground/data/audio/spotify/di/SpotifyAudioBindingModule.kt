package com.alexrdclement.mediaplayground.data.audio.spotify.di

import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepository
import com.alexrdclement.mediaplayground.data.audio.spotify.SpotifyAudioRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SpotifyAudioBindingModule {
    @Binds
    abstract fun bindSpotifyAudioRepository(
        spotifyAudioRepositoryImpl: SpotifyAudioRepositoryImpl
    ): SpotifyAudioRepository
}
