package com.alexrdclement.mediaplayground.media.engine.di

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl
import com.alexrdclement.mediaplayground.media.engine.MediaEngineControlImpl
import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.MediaEngineStateImpl
import com.alexrdclement.mediaplayground.media.engine.PlaylistControl
import com.alexrdclement.mediaplayground.media.engine.PlaylistControlImpl
import com.alexrdclement.mediaplayground.media.engine.PlaylistState
import com.alexrdclement.mediaplayground.media.engine.PlaylistStateImpl
import com.alexrdclement.mediaplayground.media.engine.TransportControl
import com.alexrdclement.mediaplayground.media.engine.TransportControlImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MediaEngineBindingModule {

    @Binds
    abstract fun bindMediaEngineControl(
        mediaEngineControlImpl: MediaEngineControlImpl,
    ): MediaEngineControl

    @Binds
    abstract fun bindMediaEngineState(
        mediaEngineStateImpl: MediaEngineStateImpl,
    ): MediaEngineState

    @Binds
    abstract fun bindTransportControl(
        transportControlImpl: TransportControlImpl,
    ): TransportControl

    @Binds
    abstract fun bindPlaylistControl(
        playlistControlImpl: PlaylistControlImpl,
    ): PlaylistControl

    @Binds
    abstract fun bindPlaylistState(
        playlistStateImpl: PlaylistStateImpl,
    ): PlaylistState
}
