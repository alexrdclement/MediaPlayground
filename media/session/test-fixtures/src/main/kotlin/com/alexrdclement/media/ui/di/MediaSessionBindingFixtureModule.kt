package com.alexrdclement.media.ui.di

import com.alexrdclement.media.ui.fakes.FakeMediaSessionControl
import com.alexrdclement.media.ui.fakes.FakeMediaSessionEntry
import com.alexrdclement.media.ui.fakes.FakeMediaSessionState
import com.alexrdclement.mediaplayground.media.session.MediaSessionControl
import com.alexrdclement.mediaplayground.media.session.MediaSessionEntry
import com.alexrdclement.mediaplayground.media.session.MediaSessionState
import com.alexrdclement.mediaplayground.media.session.di.MediaSessionBindingModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MediaSessionBindingModule::class],
)
abstract class MediaSessionBindingFixtureModule {
    @Binds
    abstract fun bindMediaSessionEntry(
        mediaSessionEntryImpl: FakeMediaSessionEntry,
    ): MediaSessionEntry

    @Binds
    abstract fun bindMediaSessionControl(
        fakeMediaSessionControl: FakeMediaSessionControl,
    ): MediaSessionControl

    @Binds
    abstract fun bindMediaSessionState(
        fakeMediaSessionState: FakeMediaSessionState,
    ): MediaSessionState
}
