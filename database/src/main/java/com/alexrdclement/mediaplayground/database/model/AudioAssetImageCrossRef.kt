package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "audio_asset_images",
    primaryKeys = ["audio_asset_id", "image_id"],
    foreignKeys = [
        ForeignKey(
            entity = AudioAsset::class,
            parentColumns = ["id"],
            childColumns = ["audio_asset_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ImageAsset::class,
            parentColumns = ["id"],
            childColumns = ["image_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["audio_asset_id"]),
        Index(value = ["image_id"]),
    ],
)
data class AudioAssetImageCrossRef(
    @ColumnInfo(name = "audio_asset_id")
    val audioAssetId: String,
    @ColumnInfo(name = "image_id")
    val imageId: String,
)
