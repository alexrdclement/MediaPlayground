package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "media_items")
data class MediaItem(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "item_type")
    val itemType: MediaItemType,
)
