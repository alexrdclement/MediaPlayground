package com.alexrdclement.mediaplayground.media.mediaimport.di

import com.alexrdclement.mediaplayground.media.mediaimport.FileWriter
import com.alexrdclement.mediaplayground.media.mediaimport.FileWriterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class FileWriterModule {
    @Binds
    abstract fun bindFileWriter(
        fileWriterImpl: FileWriterImpl
    ): FileWriter
}
