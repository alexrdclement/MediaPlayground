package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "albums",
)
data class Album(
    @PrimaryKey
    val id: String,
    val title: String,
    val modifiedDate: Instant,
)
