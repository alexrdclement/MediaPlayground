package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "albums",
    foreignKeys = [
        ForeignKey(
            entity = MediaCollection::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class Album(
    @PrimaryKey
    val id: String,
    val notes: String?,
)
