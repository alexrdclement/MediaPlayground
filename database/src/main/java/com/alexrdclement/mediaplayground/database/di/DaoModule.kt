package com.alexrdclement.mediaplayground.database.di

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
        fun provideAlbumTrackDao(database: MediaPlaygroundDatabase): AlbumTrackDao = database.albumTrackDao()

        @Provides
        fun provideImageAssetDao(database: MediaPlaygroundDatabase): ImageAssetDao = database.imageAssetDao()

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
        fun provideAudioAssetArtistDao(database: MediaPlaygroundDatabase): AudioAssetArtistDao = database.audioAssetArtistDao()

        @Provides
        fun provideAudioAssetDao(database: MediaPlaygroundDatabase): AudioAssetDao = database.audioAssetDao()

        @Provides
        fun provideAudioAssetImageDao(database: MediaPlaygroundDatabase): AudioAssetImageDao = database.audioAssetImageDao()

        @Provides
        fun provideAudioClipDao(database: MediaPlaygroundDatabase): AudioClipDao = database.audioClipDao()

        @Provides
        fun provideClipDao(database: MediaPlaygroundDatabase): ClipDao = database.clipDao()

        @Provides
        fun provideCompleteAudioClipDao(
            database: MediaPlaygroundDatabase,
        ): CompleteAudioClipDao = database.completeAudioClipDao()

        @Provides
        fun provideTrackClipDao(database: MediaPlaygroundDatabase): TrackClipDao = database.trackClipDao()

        @Provides
        fun provideMediaAssetDao(database: MediaPlaygroundDatabase): MediaAssetDao = database.mediaAssetDao()

        @Provides
        fun provideMediaAssetSyncStateDao(database: MediaPlaygroundDatabase): MediaAssetSyncStateDao = database.mediaAssetSyncStateDao()

        @Provides
        fun provideMediaCollectionDao(database: MediaPlaygroundDatabase): MediaCollectionDao = database.mediaCollectionDao()

        @Provides
        fun provideMediaItemDao(database: MediaPlaygroundDatabase): MediaItemDao = database.mediaItemDao()
    }
}
