package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "images",
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
        ),
    ]
)
data class Image(
    @PrimaryKey
    val id: String,
    val uri: String,
    val albumId: String,
)
