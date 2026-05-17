package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "media_items")
data class MediaItem(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "item_type")
    val itemType: MediaItemType,
    val title: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Instant,
)
