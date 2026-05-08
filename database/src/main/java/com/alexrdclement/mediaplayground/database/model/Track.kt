package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = MediaCollection::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class Track(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
    @ColumnInfo(name = "modified_at")
    val modifiedAt: Instant,
    val notes: String?,
)
