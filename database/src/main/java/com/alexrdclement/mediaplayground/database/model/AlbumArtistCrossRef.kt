package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "album_artists",
    primaryKeys = ["album_id", "artist_id"],
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["album_id"],
        ),
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["id"],
            childColumns = ["artist_id"],
        ),
    ],
    indices = [
        Index(value = ["album_id"]),
        Index(value = ["artist_id"]),
    ],
)
data class AlbumArtistCrossRef(
    @ColumnInfo(name = "album_id")
    val albumId: String,
    @ColumnInfo(name = "artist_id")
    val artistId: String,
)
