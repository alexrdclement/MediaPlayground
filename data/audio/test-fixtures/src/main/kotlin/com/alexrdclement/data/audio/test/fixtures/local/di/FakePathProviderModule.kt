package com.alexrdclement.data.audio.test.fixtures.local.di

import com.alexrdclement.data.audio.test.fixtures.local.FakePathProvider
import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import com.alexrdclement.mediaplayground.data.audio.local.PathProviderImpl
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
