package com.alexrdclement.mediaplayground.database.converter

import androidx.room.TypeConverter
import com.alexrdclement.mediaplayground.media.model.MediaAssetOriginUri

class MediaAssetOriginUriConverter {
    @TypeConverter
    fun fromString(value: String): MediaAssetOriginUri =
        MediaAssetOriginUri.AndroidContentUri(value)

    @TypeConverter
    fun toString(uri: MediaAssetOriginUri): String = uri.value
}
