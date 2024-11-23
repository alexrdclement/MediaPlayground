package com.alexrdclement.mediaplayground.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alexrdclement.mediaplayground.database.converter.InstantConverter
import com.alexrdclement.mediaplayground.database.dao.TrackDao
import com.alexrdclement.mediaplayground.database.model.Track

@Database(
    entities = [
        Track::class,
    ],
    version = 1,
)
@TypeConverters(
    InstantConverter::class,
)
internal abstract class MediaPlaygroundDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
}
