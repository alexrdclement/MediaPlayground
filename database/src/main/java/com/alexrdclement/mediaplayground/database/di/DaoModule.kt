package com.alexrdclement.mediaplayground.database.di

import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
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

    @Provides
    fun provideAlbumDao(database: MediaPlaygroundDatabase): AlbumDao = database.albumDao()

    @Provides
    fun provideArtistDao(database: MediaPlaygroundDatabase): ArtistDao = database.artistDao()

    @Provides
    fun provideCompleteTrackDao(
        database: MediaPlaygroundDatabase,
    ): CompleteTrackDao = database.completeTrackDao()
}
