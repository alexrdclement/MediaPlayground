package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "album_images",
    primaryKeys = ["album_id", "image_id"],
    foreignKeys = [
        ForeignKey(
            entity = Album::class,
            parentColumns = ["id"],
            childColumns = ["album_id"],
        ),
        ForeignKey(
            entity = Image::class,
            parentColumns = ["id"],
            childColumns = ["image_id"],
        ),
    ],
    indices = [
        Index(value = ["album_id"]),
        Index(value = ["image_id"]),
    ],
)
data class AlbumImageCrossRef(
    @ColumnInfo(name = "album_id")
    val albumId: String,
    @ColumnInfo(name = "image_id")
    val imageId: String,
)
