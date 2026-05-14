package com.alexrdclement.mediaplayground.database.converter

import androidx.room.TypeConverter
import com.alexrdclement.mediaplayground.database.model.MediaAssetType

class MediaTypeConverter {
    @TypeConverter
    fun fromString(value: String): MediaAssetType = enumValueOf(value)

    @TypeConverter
    fun toName(mediaAssetType: MediaAssetType): String = mediaAssetType.name
}
