package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",
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
data class Image(
    @PrimaryKey
    val id: String,
    val fileName: String,
    val albumId: String,
)
