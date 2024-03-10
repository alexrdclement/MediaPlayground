package com.alexrdclement.mediaplayground.data.audio.local.di

import com.alexrdclement.mediaplayground.data.audio.local.PathProvider
import com.alexrdclement.mediaplayground.data.audio.local.PathProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PathProviderModule {
    @Binds
    abstract fun bindPathProvider(
        pathProviderImpl: PathProviderImpl,
    ): PathProvider
}
