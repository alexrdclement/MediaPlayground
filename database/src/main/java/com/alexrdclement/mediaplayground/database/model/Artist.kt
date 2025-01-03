package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "artists",
)
data class Artist(
    @PrimaryKey
    val id: String,
    val name: String?,
)
