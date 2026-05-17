package com.alexrdclement.mediaplayground.database.converter

import androidx.room.TypeConverter
import com.alexrdclement.mediaplayground.database.model.MediaCollectionType

class MediaCollectionTypeConverter {
    @TypeConverter
    fun fromString(value: String): MediaCollectionType = enumValueOf(value)

    @TypeConverter
    fun toName(mediaCollectionType: MediaCollectionType): String = mediaCollectionType.name
}
