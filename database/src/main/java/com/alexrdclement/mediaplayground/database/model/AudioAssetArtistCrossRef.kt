package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "audio_asset_artists",
    primaryKeys = ["audio_asset_id", "artist_id"],
    foreignKeys = [
        ForeignKey(
            entity = AudioAsset::class,
            parentColumns = ["id"],
            childColumns = ["audio_asset_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Artist::class,
            parentColumns = ["id"],
            childColumns = ["artist_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["audio_asset_id"]),
        Index(value = ["artist_id"]),
    ],
)
data class AudioAssetArtistCrossRef(
    @ColumnInfo(name = "audio_asset_id")
    val audioAssetId: String,
    @ColumnInfo(name = "artist_id")
    val artistId: String,
)
