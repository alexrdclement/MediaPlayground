package com.alexrdclement.mediaplayground.database.converter

import androidx.room.TypeConverter
import com.alexrdclement.mediaplayground.database.model.MediaItemType

class MediaItemTypeConverter {
    @TypeConverter
    fun fromString(value: String): MediaItemType = enumValueOf(value)

    @TypeConverter
    fun toName(mediaItemType: MediaItemType): String = mediaItemType.name
}
