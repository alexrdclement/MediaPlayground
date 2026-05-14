package com.alexrdclement.mediaplayground.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "image_assets",
    foreignKeys = [
        ForeignKey(
            entity = MediaAsset::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class ImageAsset(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "width_px")
    val widthPx: Int,
    @ColumnInfo(name = "height_px")
    val heightPx: Int,
    @ColumnInfo(name = "date_time_original")
    val dateTimeOriginal: String?,
    @ColumnInfo(name = "gps_latitude")
    val gpsLatitude: Double?,
    @ColumnInfo(name = "gps_longitude")
    val gpsLongitude: Double?,
    @ColumnInfo(name = "camera_make")
    val cameraMake: String?,
    @ColumnInfo(name = "camera_model")
    val cameraModel: String?,
    val notes: String?,
)
