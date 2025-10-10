package com.alexrdclement.mediaplayground.media.engine.di

import com.alexrdclement.mediaplayground.media.engine.MediaEngineControl
import com.alexrdclement.mediaplayground.media.engine.MediaEngineState
import com.alexrdclement.mediaplayground.media.engine.MediaItemControl
import com.alexrdclement.mediaplayground.media.engine.MediaItemState
import com.alexrdclement.mediaplayground.media.engine.TransportControl
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeMediaEngineControl
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeMediaEngineState
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeMediaItemControl
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeMediaItemState
import com.alexrdclement.mediaplayground.media.engine.fakes.FakeTransportControl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MediaEngineBindingModule::class],
)
abstract class MediaEngineBindingFixtureModule {
    @Binds
    abstract fun bindMediaEngineControl(
        mediaEngineControl: FakeMediaEngineControl,
    ): MediaEngineControl

    @Binds
    abstract fun bindMediaEngineState(
        mediaEngineState: FakeMediaEngineState,
    ): MediaEngineState

    @Binds
    abstract fun bindTransportControl(
        transportControlImpl: FakeTransportControl,
    ): TransportControl

    @Binds
    abstract fun bindMediaItemControl(
        mediaItemLoaderImpl: FakeMediaItemControl,
    ): MediaItemControl

    @Binds
    abstract fun bindMediaItemState(
        mediaItemStateImpl: FakeMediaItemState,
    ): MediaItemState
}
