package com.alexrdclement.mediaplayground.database.converter

import androidx.room.TypeConverter
import com.alexrdclement.mediaplayground.media.model.MediaAssetUri

class MediaAssetUriConverter {
    @TypeConverter
    fun fromString(value: String): MediaAssetUri = MediaAssetUri.fromUriString(value)

    @TypeConverter
    fun toString(uri: MediaAssetUri): String = uri.toUriString()
}
