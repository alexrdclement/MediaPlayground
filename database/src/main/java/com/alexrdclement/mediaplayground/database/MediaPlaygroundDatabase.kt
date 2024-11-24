package com.alexrdclement.mediaplayground.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexrdclement.mediaplayground.database.converter.InstantConverter
import com.alexrdclement.mediaplayground.database.dao.AlbumDao
import com.alexrdclement.mediaplayground.database.dao.ArtistDao
import com.alexrdclement.mediaplayground.database.dao.CompleteTrackDao
import com.alexrdclement.mediaplayground.database.dao.ImageDao
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.model.Album
import com.alexrdclement.mediaplayground.database.model.Artist
import com.alexrdclement.mediaplayground.database.model.CompleteTrack
import com.alexrdclement.mediaplayground.database.model.Image
import com.alexrdclement.mediaplayground.database.model.Track

@Database(
    entities = [
        Track::class,
        Album::class,
        Artist::class,
        Image::class,
    ],
    views = [
        CompleteTrack::class,
    ],
    version = 1,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class MediaPlaygroundDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun albumDao(): AlbumDao
    abstract fun imageDao(): ImageDao
    abstract fun artistDao(): ArtistDao
    abstract fun completeTrackDao(): CompleteTrackDao
}
