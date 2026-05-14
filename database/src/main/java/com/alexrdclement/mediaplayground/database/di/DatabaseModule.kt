package com.alexrdclement.mediaplayground.database.di

import android.app.Application
import androidx.room.Room
import com.alexrdclement.mediaplayground.database.MediaPlaygroundDatabase
import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.AlbumImageDao
import com.alexrdclement.mediaplayground.database.dao.AlbumTrackDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.AudioAssetArtistDao
import com.alexrdclement.mediaplayground.database.dao.AudioAssetDao
import com.alexrdclement.mediaplayground.database.dao.AudioAssetImageDao
import com.alexrdclement.mediaplayground.database.dao.AudioClipDao
import com.alexrdclement.mediaplayground.database.dao.ClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAlbumDao
import com.alexrdclement.mediaplayground.database.dao.CompleteAudioClipDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageAssetDao
import com.alexrdclement.mediaplayground.database.dao.MediaAssetDao
import com.alexrdclement.mediaplayground.database.dao.MediaAssetSyncStateDao
import com.alexrdclement.mediaplayground.database.dao.MediaCollectionDao
import com.alexrdclement.mediaplayground.database.dao.MediaItemDao
import com.alexrdclement.mediaplayground.database.dao.SimpleAlbumDao
import com.alexrdclement.mediaplayground.database.dao.TrackClipDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunner
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionRunnerImpl
import com.alexrdclement.mediaplayground.database.transaction.DatabaseTransactionScope
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
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

        @Provides
        @SingleIn(AppScope::class)
        fun provideDatabaseTransactionScope(
            albumDao: AlbumDao,
            albumArtistDao: AlbumArtistDao,
            albumImageDao: AlbumImageDao,
            albumTrackDao: AlbumTrackDao,
            artistDao: ArtistDao,
            audioAssetArtistDao: AudioAssetArtistDao,
            audioAssetDao: AudioAssetDao,
            audioAssetImageDao: AudioAssetImageDao,
            audioClipDao: AudioClipDao,
            clipDao: ClipDao,
            completeAlbumDao: CompleteAlbumDao,
            completeAudioClipDao: CompleteAudioClipDao,
            completeTrackDao: CompleteTrackDao,
            imageAssetDao: ImageAssetDao,
            mediaAssetDao: MediaAssetDao,
            mediaAssetSyncStateDao: MediaAssetSyncStateDao,
            mediaCollectionDao: MediaCollectionDao,
            mediaItemDao: MediaItemDao,
            simpleAlbumDao: SimpleAlbumDao,
            trackClipDao: TrackClipDao,
            trackDao: TrackDao,
        ): DatabaseTransactionScope = object : DatabaseTransactionScope {
            override val albumDao = albumDao
            override val albumArtistDao = albumArtistDao
            override val albumImageDao = albumImageDao
            override val albumTrackDao = albumTrackDao
            override val artistDao = artistDao
            override val audioAssetArtistDao = audioAssetArtistDao
            override val audioAssetDao = audioAssetDao
            override val audioAssetImageDao = audioAssetImageDao
            override val audioClipDao = audioClipDao
            override val clipDao = clipDao
            override val completeAlbumDao = completeAlbumDao
            override val completeAudioClipDao = completeAudioClipDao
            override val completeTrackDao = completeTrackDao
            override val imageAssetDao = imageAssetDao
            override val mediaAssetDao = mediaAssetDao
            override val mediaAssetSyncStateDao = mediaAssetSyncStateDao
            override val mediaCollectionDao = mediaCollectionDao
            override val mediaItemDao = mediaItemDao
            override val simpleAlbumDao = simpleAlbumDao
            override val trackClipDao = trackClipDao
            override val trackDao = trackDao
        }

        @Provides
        fun provideDatabaseTransactionRunner(
            database: MediaPlaygroundDatabase,
            scope: DatabaseTransactionScope,
        ): DatabaseTransactionRunner = DatabaseTransactionRunnerImpl(database, scope)
    }
}
