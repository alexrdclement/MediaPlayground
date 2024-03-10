package com.alexrdclement.media.session.test.fixtures.di

import com.alexrdclement.media.session.test.fixtures.FakeMediaSessionManager
import com.alexrdclement.mediaplayground.media.session.MediaSessionManager
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
    abstract fun bindMediaSessionManager(
        fakeMediaSessionManager: FakeMediaSessionManager,
    ): MediaSessionManager
}
