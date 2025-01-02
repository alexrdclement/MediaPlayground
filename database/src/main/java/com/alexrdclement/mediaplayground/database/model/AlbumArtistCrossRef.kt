package com.alexrdclement.mediaplayground.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "albumArtists",
    primaryKeys = ["albumId", "artistId"],
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
        ),
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
        ),
    ],
    indices = [
        Index(value = ["albumId"]),
        Index(value = ["artistId"]),
    ],
)
data class AlbumArtistCrossRef(
    val albumId: String,
    val artistId: String,
)
