package com.alexrdclement.mediaplayground.database.converter

import androidx.room.TypeConverter
import com.alexrdclement.mediaplayground.media.model.MediaAssetSyncState

class MediaAssetSyncStateConverter {
    @TypeConverter
    fun fromString(value: String): MediaAssetSyncState = enumValueOf(value)

    @TypeConverter
    fun toName(syncState: MediaAssetSyncState): String = syncState.name
}
