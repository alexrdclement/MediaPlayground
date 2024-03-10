package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetriever
import com.alexrdclement.mediaplayground.media.mediaimport.MediaMetadataRetrieverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaMetadataRetrieverModule {
    @Binds
    abstract fun bindMediaMetadataRetriever(
        mediaMetadataRetrieverImpl: MediaMetadataRetrieverImpl
    ): MediaMetadataRetriever
}
