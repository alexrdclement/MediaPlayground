package com.alexrdclement.mediaplayground.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexrdclement.mediaplayground.database.converter.InstantConverter
import com.alexrdclement.mediaplayground.database.converter.MediaAssetOriginUriConverter
import com.alexrdclement.mediaplayground.database.converter.MediaAssetSyncStateConverter
import com.alexrdclement.mediaplayground.database.converter.MediaAssetUriConverter
import com.alexrdclement.mediaplayground.database.converter.MediaCollectionTypeConverter
import com.alexrdclement.mediaplayground.database.converter.MediaItemTypeConverter
import com.alexrdclement.mediaplayground.database.converter.MediaTypeConverter
import com.alexrdclement.mediaplayground.database.dao.AlbumArtistDao
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.AlbumImageDao
import com.alexrdclement.mediaplayground.database.dao.AlbumTrackDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.AudioAssetDao
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
import com.alexrdclement.mediaplayground.database.model.Album
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumTrackCrossRef
import com.alexrdclement.mediaplayground.database.model.Artist
import com.alexrdclement.mediaplayground.database.model.AudioAsset
import com.alexrdclement.mediaplayground.database.model.Clip
import com.alexrdclement.mediaplayground.database.model.ImageAsset
import com.alexrdclement.mediaplayground.database.model.MediaAsset
import com.alexrdclement.mediaplayground.database.model.MediaAssetSyncStateEntity
import com.alexrdclement.mediaplayground.database.model.MediaCollection
import com.alexrdclement.mediaplayground.database.model.MediaItem
import com.alexrdclement.mediaplayground.database.model.Track
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef

@Database(
    entities = [
        Album::class,
        AlbumArtistCrossRef::class,
        AlbumImageCrossRef::class,
        AlbumTrackCrossRef::class,
        Artist::class,
        AudioAsset::class,
        Clip::class,
        ImageAsset::class,
        MediaAsset::class,
        MediaAssetSyncStateEntity::class,
        MediaCollection::class,
        MediaItem::class,
        Track::class,
        TrackClipCrossRef::class,
    ],
    version = 1,
)
@TypeConverters(
    InstantConverter::class,
    MediaAssetOriginUriConverter::class,
    MediaAssetSyncStateConverter::class,
    MediaAssetUriConverter::class,
    MediaCollectionTypeConverter::class,
    MediaItemTypeConverter::class,
    MediaTypeConverter::class,
)
abstract class MediaPlaygroundDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun albumArtistDao(): AlbumArtistDao
    abstract fun albumTrackDao(): AlbumTrackDao
    abstract fun albumImageDao(): AlbumImageDao
    abstract fun artistDao(): ArtistDao
    abstract fun audioAssetDao(): AudioAssetDao
    abstract fun clipDao(): ClipDao
    abstract fun completeAlbumDao(): CompleteAlbumDao
    abstract fun completeAudioClipDao(): CompleteAudioClipDao
    abstract fun completeTrackDao(): CompleteTrackDao
    abstract fun imageAssetDao(): ImageAssetDao
    abstract fun mediaAssetDao(): MediaAssetDao
    abstract fun mediaAssetSyncStateDao(): MediaAssetSyncStateDao
    abstract fun mediaCollectionDao(): MediaCollectionDao
    abstract fun mediaItemDao(): MediaItemDao
    abstract fun simpleAlbumDao(): SimpleAlbumDao
    abstract fun trackClipDao(): TrackClipDao
    abstract fun trackDao(): TrackDao
}
