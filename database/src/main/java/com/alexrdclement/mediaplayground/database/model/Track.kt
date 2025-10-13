package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Instant

@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["album_id"],
        ),
    ],
    indices = [
        Index(value = ["album_id"]),
    ],
)
data class Track(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "file_name")
    val fileName: String?,
    val title: String,
    @ColumnInfo(name = "album_id")
    val albumId: String,
    @ColumnInfo(name = "duration_ms")
    val durationMs: Int,
    @ColumnInfo(name = "track_number")
    val trackNumber: Int?,
    @ColumnInfo(name = "modified_date")
    val modifiedDate: Instant,
    val source: Source,
)
