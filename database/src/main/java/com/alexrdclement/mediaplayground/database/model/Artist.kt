package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "artists",
)
data class Artist(
    @PrimaryKey
    val id: String,
    val name: String?,
)
