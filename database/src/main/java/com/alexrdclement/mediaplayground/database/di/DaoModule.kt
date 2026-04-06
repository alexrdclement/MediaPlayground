package com.alexrdclement.mediaplayground.database.di

import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.AlbumImageDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.AudioFileDao
import com.alexrdclement.mediaplayground.database.dao.ClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAudioClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageFileDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.dao.TrackClipDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides

@BindingContainer
interface DaoModule {
    companion object {
        @Provides
        fun provideTrackDao(database: MediaPlaygroundDatabase): TrackDao = database.trackDao()

        @Provides
        fun provideAlbumDao(database: MediaPlaygroundDatabase): AlbumDao = database.albumDao()

        @Provides
        fun provideImageDao(database: MediaPlaygroundDatabase): ImageFileDao = database.imageDao()

        @Provides
        fun provideArtistDao(database: MediaPlaygroundDatabase): ArtistDao = database.artistDao()

        @Provides
        fun provideCompleteTrackDao(
            database: MediaPlaygroundDatabase,
        ): CompleteTrackDao = database.completeTrackDao()

        @Provides
        fun provideCompleteAlbumDao(
            database: MediaPlaygroundDatabase,
        ): CompleteAlbumDao = database.completeAlbumDao()

        @Provides
        fun provideAlbumArtistDao(
            database: MediaPlaygroundDatabase,
        ): AlbumArtistDao = database.albumArtistDao()

        @Provides
        fun provideSimpleAlbumDao(
            database: MediaPlaygroundDatabase,
        ): SimpleAlbumDao = database.simpleAlbumDao()

        @Provides
        fun provideAlbumImageDao(
            database: MediaPlaygroundDatabase,
        ): AlbumImageDao = database.albumImageDao()

        @Provides
        fun provideAudioFileDao(database: MediaPlaygroundDatabase): AudioFileDao = database.audioFileDao()

        @Provides
        fun provideClipDao(database: MediaPlaygroundDatabase): ClipDao = database.clipDao()

        @Provides
        fun provideCompleteAudioClipDao(
            database: MediaPlaygroundDatabase,
        ): CompleteAudioClipDao = database.completeAudioClipDao()

        @Provides
        fun provideTrackClipDao(database: MediaPlaygroundDatabase): TrackClipDao = database.trackClipDao()
    }
}
