package com.alexrdclement.mediaplayground.data.audio.local.di

import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepository
import com.alexrdclement.mediaplayground.data.audio.local.LocalAudioRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalAudioBindingModule {
    @Binds
    abstract fun bindLocalAudioRepository(
        localAudioRepositoryImpl: LocalAudioRepositoryImpl
    ): LocalAudioRepository
}
