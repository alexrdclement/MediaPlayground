package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(
    tableName = "albums",
)
data class Album(
    @PrimaryKey
    val id: String,
    val title: String,
    @ColumnInfo(name = "modified_date")
    val modifiedDate: Instant,
    val source: Source,
)
