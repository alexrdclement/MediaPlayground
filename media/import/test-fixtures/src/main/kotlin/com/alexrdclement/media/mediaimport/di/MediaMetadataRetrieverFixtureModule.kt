package com.alexrdclement.media.mediaimport.di

import com.alexrdclement.media.mediaimport.fakes.FakeMediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetriever
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaMetadataRetrieverFixtureModule {
    @Binds
    abstract fun bindMediaMetadataRetriever(
        fakeMediaMetadataRetriever: FakeMediaMetadataRetriever
    ): MediaMetadataRetriever
}
