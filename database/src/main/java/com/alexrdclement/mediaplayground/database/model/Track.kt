package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "track",
)
data class Track(
    @PrimaryKey
    val id: String,
    val title: String,
    val durationMs: Int,
    val trackNumber: Int?,
    val uri: String?,
    val modifiedDate: Instant,
)
