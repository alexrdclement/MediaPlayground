package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "media_item_images",
    primaryKeys = ["item_id", "image_asset_id"],
    foreignKeys = [
        ForeignKey(
            entity = MediaItem::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ImageAsset::class,
            parentColumns = ["id"],
            childColumns = ["image_asset_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["item_id"]),
        Index(value = ["image_asset_id"]),
    ],
)
data class MediaItemImageCrossRef(
    @ColumnInfo(name = "item_id")
    val itemId: String,
    @ColumnInfo(name = "image_asset_id")
    val imageAssetId: String,
)
