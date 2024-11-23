package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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
)
