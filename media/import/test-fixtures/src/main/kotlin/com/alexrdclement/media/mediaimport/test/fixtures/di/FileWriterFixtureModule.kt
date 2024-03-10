package com.alexrdclement.media.mediaimport.test.fixtures.di

import com.alexrdclement.media.mediaimport.test.fixtures.FakeFileWriter
import com.alexrdclement.mediaplayground.media.mediaimport.FileWriter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FileWriterFixtureModule {
    @Binds
    abstract fun bindFileWriter(
        fakeFileWriter: FakeFileWriter
    ): FileWriter
}
