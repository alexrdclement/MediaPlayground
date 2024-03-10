package com.alexrdclement.mediaplayground.data.audio.local.di

import com.alexrdclement.mediaplayground.data.audio.local.fakes.FakePathProvider
import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FakePathProviderModule {
    @Binds
    abstract fun bindPathProvider(
        pathProviderImpl: FakePathProvider,
    ): PathProvider
}
