package com.alexrdclement.mediaplayground.media.session.di

import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionControlImpl
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.MediaSessionStateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaSessionBindingModule {

    @Binds
    abstract fun bindMediaSessionControl(
        mediaSessionControlImpl: MediaSessionControlImpl
    ): MediaSessionControl

    @Binds
    abstract fun bindMediaSessionState(
        mediaSessionStateImpl: MediaSessionStateImpl
    ): MediaSessionState
}
