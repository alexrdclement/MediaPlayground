package com.alexrdclement.media.mediaimport.di

import com.alexrdclement.media.mediaimport.fakes.FakeMediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.di.MediaMetadataRetrieverModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MediaMetadataRetrieverModule::class],
)
abstract class MediaMetadataRetrieverFixtureModule {
    @Binds
    abstract fun bindMediaMetadataRetriever(
        fakeMediaMetadataRetriever: FakeMediaMetadataRetriever
    ): MediaMetadataRetriever
}
