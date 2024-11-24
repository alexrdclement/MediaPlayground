package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(
    tableName = "albums",
    foreignKeys = [
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
        ),
    ]
)
data class Album(
    @PrimaryKey
    val id: String,
    val title: String,
    val artistId: String,
    val modifiedDate: Instant,
)
