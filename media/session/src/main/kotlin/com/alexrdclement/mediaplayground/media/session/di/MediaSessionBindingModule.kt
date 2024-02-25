package com.alexrdclement.mediaplayground.media.session.di

import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
import com.alexrdclement.mediaplayground.media.session.MediaSessionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaSessionBindingModule {

    @Binds
    abstract fun bindMediaSessionManager(
        mediaSessionManagerImpl: MediaSessionManagerImpl
    ): MediaSessionManager
}
