package com.alexrdclement.mediaplayground.database.di

import android.app.Application
import androidx.room.Room
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
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunnerImpl
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionScope
import com.alexrdclement.mediaplayground.database.transaction.MediaStoreTransactionRunnerImpl
import com.alexrdclement.mediaplayground.media.store.MediaStoreTransactionRunner
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@BindingContainer
interface DatabaseModule {
    companion object {
        @Provides
        @SingleIn(AppScope::class)
        fun provideMediaPlaygroundDatabase(
            application: Application,
        ): MediaPlaygroundDatabase = Room.databaseBuilder(
            application,
            MediaPlaygroundDatabase::class.java,
            "mediaplayground-database",
        ).addMigrations(
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

        @Provides
        @SingleIn(AppScope::class)
        fun provideDatabaseTransactionScope(
            albumDao: AlbumDao,
            albumArtistDao: AlbumArtistDao,
            albumImageDao: AlbumImageDao,
            artistDao: ArtistDao,
            audioFileDao: AudioFileDao,
            clipDao: ClipDao,
            completeAlbumDao: CompleteAlbumDao,
            completeAudioClipDao: CompleteAudioClipDao,
            completeTrackDao: CompleteTrackDao,
            imageDao: ImageFileDao,
            simpleAlbumDao: SimpleAlbumDao,
            trackClipDao: TrackClipDao,
            trackDao: TrackDao,
        ): DatabaseTransactionScope = object : DatabaseTransactionScope {
            override val albumDao = albumDao
            override val albumArtistDao = albumArtistDao
            override val albumImageDao = albumImageDao
            override val artistDao = artistDao
            override val audioFileDao = audioFileDao
            override val clipDao = clipDao
            override val completeAlbumDao = completeAlbumDao
            override val completeAudioClipDao = completeAudioClipDao
            override val completeTrackDao = completeTrackDao
            override val imageFileDao = imageDao
            override val simpleAlbumDao = simpleAlbumDao
            override val trackClipDao = trackClipDao
            override val trackDao = trackDao
        }

        @Provides
        fun provideDatabaseTransactionRunner(
            database: MediaPlaygroundDatabase,
            scope: DatabaseTransactionScope,
        ): DatabaseTransactionRunner = DatabaseTransactionRunnerImpl(database, scope)

        @Provides
        fun provideMediaStoreTransactionRunner(
            database: MediaPlaygroundDatabase,
        ): MediaStoreTransactionRunner = MediaStoreTransactionRunnerImpl(database)
    }
}
