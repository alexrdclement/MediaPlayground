package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "tracks",
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
        ),
    ],
    indices = [
        Index(value = ["albumId"]),
    ],
)
data class Track(
    @PrimaryKey
    val id: String,
    val fileName: String?,
    val title: String,
    val albumId: String,
    val durationMs: Int,
    val trackNumber: Int?,
    val modifiedDate: Instant,
)
