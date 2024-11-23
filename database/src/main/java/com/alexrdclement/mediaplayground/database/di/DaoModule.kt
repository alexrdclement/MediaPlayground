package com.alexrdclement.mediaplayground.database.di

import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal class DaoModule {
    @Provides
    fun provideTrackDao(database: MediaPlaygroundDatabase): TrackDao = database.trackDao()
}
