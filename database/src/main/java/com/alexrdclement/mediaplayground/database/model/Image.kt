package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "images",
)
data class Image(
    @PrimaryKey
    val id: String,
    val uri: String,
)
