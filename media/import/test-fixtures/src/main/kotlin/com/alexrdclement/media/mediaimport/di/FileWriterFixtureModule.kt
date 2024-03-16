package com.alexrdclement.media.mediaimport.di

import com.alexrdclement.media.mediaimport.fakes.FakeFileWriter
import com.alexrdclement.mediaplayground.media.mediaimport.FileWriter
import com.alexrdclement.mediaplayground.media.mediaimport.di.FileWriterModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [FileWriterModule::class],
)
abstract class FileWriterFixtureModule {
    @Binds
    abstract fun bindFileWriter(
        fakeFileWriter: FakeFileWriter
    ): FileWriter
}
