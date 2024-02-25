package com.alexrdclement.mediaplayground.mediasession.di

import com.alexrdclement.mediaplayground.mediasession.MediaSessionManager
import com.alexrdclement.mediaplayground.mediasession.MediaSessionManagerImpl
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
