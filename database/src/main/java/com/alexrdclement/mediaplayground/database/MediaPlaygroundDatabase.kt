package com.alexrdclement.mediaplayground.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexrdclement.mediaplayground.database.converter.InstantConverter
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
import com.alexrdclement.mediaplayground.database.model.Album
import com.alexrdclement.mediaplayground.database.model.AlbumArtistCrossRef
import com.alexrdclement.mediaplayground.database.model.AlbumImageCrossRef
import com.alexrdclement.mediaplayground.database.model.Artist
import com.alexrdclement.mediaplayground.database.model.AudioFile
import com.alexrdclement.mediaplayground.database.model.Clip
import com.alexrdclement.mediaplayground.database.model.ImageFile
import com.alexrdclement.mediaplayground.database.model.Track
import com.alexrdclement.mediaplayground.database.model.TrackClipCrossRef

@Database(
    entities = [
        Track::class,
        Album::class,
        Artist::class,
        ImageFile::class,
        AlbumArtistCrossRef::class,
        AlbumImageCrossRef::class,
        AudioFile::class,
        Clip::class,
        TrackClipCrossRef::class,
    ],
    version = 1,
)
@TypeConverters(
    InstantConverter::class,
)
abstract class MediaPlaygroundDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun albumDao(): AlbumDao
    abstract fun imageDao(): ImageFileDao
    abstract fun artistDao(): ArtistDao
    abstract fun albumImageDao(): AlbumImageDao
    abstract fun completeTrackDao(): CompleteTrackDao
    abstract fun completeAlbumDao(): CompleteAlbumDao
    abstract fun albumArtistDao(): AlbumArtistDao
    abstract fun simpleAlbumDao(): SimpleAlbumDao
    abstract fun audioFileDao(): AudioFileDao
    abstract fun clipDao(): ClipDao
    abstract fun completeAudioClipDao(): CompleteAudioClipDao
    abstract fun trackClipDao(): TrackClipDao
}
